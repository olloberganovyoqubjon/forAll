import os
from flask import Flask, request, send_file # type: ignore
from docx import Document # type: ignore
from io import BytesIO
import transliterate
from flask_cors import CORS # type: ignore
import win32com.client as win32 # type: ignore
from werkzeug.utils import secure_filename # type: ignore
import pythoncom # type: ignore
from py_eureka_client import eureka_client # type: ignore
from dotenv import load_dotenv
load_dotenv()

app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'
app.config['UPLOAD_FOLDER'] = 'uploads'  # Directory for uploaded files

# Ensure the upload folder exists
if not os.path.exists(app.config['UPLOAD_FOLDER']):
    os.makedirs(app.config['UPLOAD_FOLDER'])


@app.route('/api/translate/upload/<lang>', methods=['POST'])
def upload_file(lang):
    if 'file' not in request.files:
        return 'No file part', 400
    file = request.files['file']
    if file.filename == '':
        return 'No selected file', 400
    
    filename = secure_filename(file.filename)
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    file.save(file_path)

    if filename.endswith('.doc'):
        # Handle .doc files using win32com.client
        translated_docx_bytes = translate_doc_file(file_path, lang)
        if translated_docx_bytes:
            return send_file(translated_docx_bytes, mimetype="application/vnd.openxmlformats-officedocument.wordprocessingml.document", download_name="translated_document.docx")
        else:
            return 'Failed to process .doc file', 500
    
    if file_path.endswith('.docx'):
        doc = Document(file_path)
        translate_document(doc, lang)
        translated_docx_bytes = BytesIO()
        doc.save(translated_docx_bytes)
        translated_docx_bytes.seek(0)
        return send_file(translated_docx_bytes, mimetype="application/vnd.openxmlformats-officedocument.wordprocessingml.document", download_name="translated_document.docx")
    
    return 'Invalid file type', 400


def translate_doc_file(doc_path, lang):
    try:
        pythoncom.CoInitialize()  # Initialize COM library
        word = win32.Dispatch("Word.Application")
        doc = word.Documents.Open(doc_path)

        # Transliterate the document text
        for paragraph in doc.Paragraphs:
            paragraph.Range.Text = transliterate.transliterate(paragraph.Range.Text, lang)
        
        # Save the document to a BytesIO object
        translated_docx_bytes = BytesIO()
        docx_path = os.path.join(app.config['UPLOAD_FOLDER'], "temp_translated.docx")
        doc.SaveAs(docx_path, FileFormat=16)  # Save as .docx (FileFormat=16)

        # Close the Word document
        doc.Close()
        word.Quit()

        # Read the saved .docx into memory and return it
        with open(docx_path, 'rb') as f:
            translated_docx_bytes.write(f.read())
        translated_docx_bytes.seek(0)

        # Clean up the temp file
        os.remove(docx_path)

        return translated_docx_bytes
    except Exception as e:
        print(f"Error processing .doc file: {e}")
        return None
    finally:
        pythoncom.CoUninitialize()  # Uninitialize COM library


def translate_paragraph(paragraph, lang):
    for run in paragraph.runs:
        run.text = transliterate.transliterate(run.text, lang)


def translate_document(doc, lang):
    for paragraph in doc.paragraphs:
        translate_paragraph(paragraph, lang)
    for table in doc.tables:
        for row in table.rows:
            for cell in row.cells:
                for paragraph in cell.paragraphs:
                    translate_paragraph(paragraph, lang)


def start_eureka_client():
    """Start an Eureka client to register this service with the Eureka server."""
    eureka_client.init(eureka_server=os.getenv("EUREKA_SERVER"),
                       app_name=os.getenv("YOUR_APP_NAME"),
                       instance_port=int(os.getenv("YOUR_INSTANCE_PORT")))

if __name__ == '__main__':
    start_eureka_client()
    app.run(debug=True, port = int(os.getenv("YOUR_INSTANCE_PORT")))
