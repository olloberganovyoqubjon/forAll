package controllers

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	db "main/db"
	format_errors "main/internal/format-errors"
	"main/internal/pagination"
	"main/internal/parsModels"
	"math/rand"
	"mime/multipart"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/gin-gonic/gin"
)

type Name struct {
	Weather  Weather
	InfList  []InfList
	RadeList []Rades
}

type Weather struct {
	TempMin           float32   `json:"tempMin"`
	TempMax           float32   `json:"tempMax"`
	WeatherNameFirst  string    `json:"weatherNameFirst"`
	WeatherNameSecond string    `json:"weatherNameSecond"`
	UpdateDate        time.Time `json:"updateDate"`
}

type InfList struct {
	Id         int
	Link       string     `json:"link"`
	Title      string     `json:"title"`
	Img        string     `json:"img"`
	Contents   []Contents `json:"contents"`
	CreateDate string     `json:"createDate"`
	Category   Category   `json:"category"`
}

type Contents struct {
	Content string `json:"content"`
}

type Category struct {
	Id           int
	Name         string `json:"name"`
	Sites        Sites  `json:"sites"`
	CategoryName string `json:"categoryName"`
}

type Sites struct {
	Id   int
	Name string `json:"name"`
}

type Rades struct {
	Date      string  `json:"date"`
	Code      string  `json:"code"`
	CbPrice   float64 `json:"cbPrice"`
	Title     string  `json:"title"`
	ByPrice   float64 `json:"byPrice"`
	CellPrice float64 `json:"cellPrice"`
}

func UploadPars(c *gin.Context) {
	// single file
	file, _ := c.FormFile("file")
	log.Println(file.Filename)

	SelectJson(file)
	c.String(http.StatusOK, fmt.Sprintf("'%s' uploaded!", file.Filename))
}

// func checkErr(err error) {
// 	if err != nil {
// 		log.Fatal(err)
// 	}
// }

func SelectJson(file *multipart.FileHeader) {
	open, err := file.Open()
	if err != nil {
		return
	}

	fmt.Println("The File is opened successfully...")

	defer open.Close()

	byteResult, _ := ioutil.ReadAll(open)

	var name Name
	// var radeList []RadeList
	json.Unmarshal(byteResult, &name)
	// json.Unmarshal(byteResult, &radeList)

	weather := name.Weather
	var weatherBase parsModels.Weather
	if err := db.ParsDB.First(&weatherBase).Error; err != nil {
		fmt.Println("Ma'lumot topilmadi yoki xato:", err)
		return
	}

	// Qatorni yangilash
	weatherBase.TempMin = weather.TempMin
	weatherBase.TempMax = weather.TempMax
	weatherBase.WeatherNameFirst = weather.WeatherNameFirst
	weatherBase.WeatherNameSecond = weather.WeatherNameSecond
	weatherBase.UpdateDate = weather.UpdateDate

	// O‘zgarishlarni saqlash
	db.ParsDB.Save(&weatherBase)

	rades := name.RadeList

	for i, rade := range rades {
		db.ParsDB.Updates(parsModels.Rade{
			Date:      rade.Date,
			Code:      rade.Code,
			CbPrice:   float32(rade.CbPrice),
			ByPrice:   float32(rade.ByPrice),
			CellPrice: float32(rade.CellPrice),
		}).Where("id", i+1)
		fmt.Println(rade)
	}

	for _, inf := range name.InfList {
		site := inf.Category.Sites
		siteModel := parsModels.Sites{
			Name: site.Name,
		}
		var siteBase parsModels.Sites
		db.ParsDB.Find(&siteBase, "name = ?", siteModel.Name)
		idSite := siteBase.ID
		if idSite == 0 {
			db.ParsDB.Create(&siteModel)
			idSite = siteModel.ID
		}

		categoryModel := parsModels.Category{
			Name:         inf.Category.Name,
			CategoryName: inf.Category.CategoryName,
			SiteId:       idSite,
		}
		var categoryBase parsModels.Category

		db.ParsDB.First(&categoryBase, "name = ?", categoryModel.Name)
		idCategory := categoryBase.ID
		if idCategory == 0 {
			db.ParsDB.Create(&categoryModel)
			idCategory = categoryModel.ID
		}
		layout := "2006-01-02T15:04:05"
		createDates := strings.Split(inf.CreateDate, ".")
		createDate, err := time.Parse(layout, createDates[0])
		if err != nil {
			fmt.Println("Error parsing time:", err)
		}

		infModel := parsModels.Inf{
			Link:        inf.Link,
			Title:       inf.Title,
			Img:         []byte(inf.Img),
			Create_date: createDate,
			CategoryId:  idCategory,
		}

		var infBase parsModels.Inf
		db.ParsDB.First(&infBase, "link = ?", infModel.Link)
		idInf := infBase.ID
		if idInf == 0 {
			db.ParsDB.Create(&infModel)
			db.ParsDB.Exec("delete from infs where id = ?", (infModel.ID - 1000))
			db.ParsDB.Exec("delete from contents where inf_id = ?", (infModel.ID - 1000))
			idInf = infModel.ID
			contentsInternal := inf.Contents
			for _, contentInternal := range contentsInternal {
				contentModel := parsModels.Contents{
					Content: contentInternal.Content,
					InfID:   idInf,
				}
				db.ParsDB.Create(&contentModel)
			}
		}
	}
}

func GetAllSitesWithCategory(c *gin.Context) {
	var sitesBase []parsModels.Sites
	db.ParsDB.Find(&sitesBase).Order("ID")
	allSitesModel := []AllSites{}

	for _, site := range sitesBase {
		categorysBase := []parsModels.Category{}
		db.ParsDB.Find(&categorysBase, "site_id = ?", site.ID).Order("id")
		allsiteModel := AllSites{
			Id:        int(site.ID),
			Name:      site.Name,
			Categorys: categorysBase,
		}
		allSitesModel = append(allSitesModel, allsiteModel)
	}

	c.JSON(http.StatusOK, gin.H{
		"allSites": allSitesModel,
	})
}

type AllSites struct {
	Id        int
	Name      string                `json:"name"`
	Categorys []parsModels.Category `json:"category"`
}

func GetPresidentNews(c *gin.Context) {
	var categoryModel parsModels.Category
	db.ParsDB.First(&categoryModel, "name = ?", "https://president.uz/uz/lists/news?menu_id=12").Order("ID")

	pageStr := c.DefaultQuery("page", "1")
	page, _ := strconv.Atoi(pageStr)
	perPageStr := c.DefaultQuery("perPage", "10")
	perPage, _ := strconv.Atoi(perPageStr)
	infsModel := []parsModels.Inf{}
	_, err := pagination.Paginate(db.ParsDB.Unscoped().Order("id desc").Where("category_id = ?", categoryModel.ID), page, perPage, nil, &infsModel)
	if err != nil {
		format_errors.InternalServerError(c)
		return
	}

	InfsList := []InfList{}

	for _, infModel := range infsModel {
		InfList := InfList{
			Id:         int(infModel.ID),
			Title:      infModel.Title,
			Img:        string(infModel.Img),
			CreateDate: infModel.Create_date.Format("2006-01-02 15:04"),
		}
		InfsList = append(InfsList, InfList)
	}

	c.JSON(http.StatusOK, gin.H{
		"infsModel": InfsList,
	})
}

func GetNewsByIdSitesAndIdCategory(c *gin.Context) {
	idSite := c.Param("idSite")
	idCategory := c.Param("idCategory")
	pageStr := c.DefaultQuery("page", "1")
	page, _ := strconv.Atoi(pageStr)
	perPageStr := c.DefaultQuery("perPage", "20")
	perPage, _ := strconv.Atoi(perPageStr)
	infsModel := []parsModels.Inf{}
	infsModel1 := []parsModels.Inf{}
	var err error
	idModel := parsModels.Inf{}
	db.ParsDB.Order("id desc").First(&idModel)
	if idSite != "0" {
		var categoryModels []parsModels.Category
		db.ParsDB.Order("id desc").Find(&categoryModels, "site_id = ?", idSite)
		// for _, category := range categoryModels {
		categoryCondition := ""
		params := []interface{}{}
		for i, category := range categoryModels {
			if i == 0 {
				categoryCondition = "category_id = ?"
			} else {
				categoryCondition += " OR category_id = ?"
			}
			params = append(params, category.ID)
		}
		_, err = pagination.Paginate(
			db.ParsDB.Unscoped().
				Order("create_date desc").
				Where(categoryCondition, params...),
			page,
			20,
			nil,
			&infsModel1,
		)
		// _, err = pagination.Paginate(initializers.ParsDB.Unscoped().Order("id desc").Where(categoryModels).Where("create_date BETWEEN ? AND ?", idModel.Create_date.AddDate(0, 0, -1), idModel.Create_date), page, 5, nil, &infsModel1)
		infsModel = append(infsModel, infsModel1...)
		// }
		rand.Shuffle(len(infsModel), func(i, j int) { infsModel[i], infsModel[j] = infsModel[j], infsModel[i] })
	} else if idCategory == "0" {
		_, err = pagination.Paginate(db.ParsDB.Unscoped().Order("create_date desc"), page, perPage, nil, &infsModel)
	} else {
		_, err = pagination.Paginate(db.ParsDB.Unscoped().Order("create_date desc").Where("category_id = ?", idCategory), page, perPage, nil, &infsModel)
	}
	if err != nil {
		format_errors.InternalServerError(c)
		return
	}

	InfsList := []InfList{}

	for _, infModel := range infsModel {
		var categoryModel parsModels.Category
		db.ParsDB.First(&categoryModel, "id = ?", infModel.CategoryId).Order("ID")
		var siteModel parsModels.Sites
		db.ParsDB.First(&siteModel, "id = ?", categoryModel.SiteId).Order("ID")
		Site := Sites{
			Id:   int(siteModel.ID),
			Name: siteModel.Name,
		}
		Category := Category{
			Id:           int(categoryModel.ID),
			Name:         categoryModel.Name,
			Sites:        Site,
			CategoryName: categoryModel.CategoryName,
		}
		InfList := InfList{
			Id:         int(infModel.ID),
			Title:      infModel.Title,
			Img:        string(infModel.Img),
			CreateDate: infModel.Create_date.Format("02.01.2006 15:04"),
			Category:   Category,
		}
		InfsList = append(InfsList, InfList)
	}

	c.JSON(http.StatusOK, gin.H{
		"infsModel": InfsList,
	})
}

type InfModel struct {
	Id         int      `json:"id"`
	Title      string   `json:"title"`
	Img        string   `json:"img"`
	Contents   []string `json:"contents"`
	CreateDate string   `json:"create_date"`
}

// type InfModelInf struct {
// 	Id       int      `json:"id"`
// 	Title    string   `json:"title"`
// 	Img      string   `json:"img"`
// 	Contents []string `json:"contents"`
// }

func GetNewsByIdSite(c *gin.Context) {
	id := c.Param("id")
	var infBase parsModels.Inf
	db.ParsDB.First(&infBase, "id = ?", id)
	var contentsBase []parsModels.Contents
	db.ParsDB.Find(&contentsBase, "inf_id = ?", infBase.ID)
	var contents []string
	for _, content := range contentsBase {
		contents = append(contents, content.Content)
	}

	infModel := InfModel{
		Title:      infBase.Title,
		Img:        string(infBase.Img),
		Contents:   contents,
		CreateDate: infBase.Create_date.Format("2006-01-02 15:04"),
	}

	c.JSON(http.StatusOK, gin.H{
		"infsModel": infModel,
	})
}

func GetWether(c *gin.Context) {
	var weatherBase parsModels.Weather
	if err := db.ParsDB.First(&weatherBase).Error; err != nil {
		fmt.Println("Ma'lumot topilmadi yoki xato:", err)
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"weather": weatherBase,
	})
}
