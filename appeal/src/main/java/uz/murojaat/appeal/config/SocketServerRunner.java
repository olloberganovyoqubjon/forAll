package uz.murojaat.appeal.config;


import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketServerRunner implements ApplicationRunner {

    private final SocketIOServer server;

    public SocketServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(ApplicationArguments args) {
        server.start();
        System.out.println("Socket.IO server ishga tushdi!");

        // Mijozdan "message" event qabul qilish va javob qaytarish
        server.addEventListener("message", String.class, (client, data, ackSender) -> {
            System.out.println("Clientdan kelgan xabar: " + data + " : " + client.getSessionId());
            client.sendEvent("message", "Salom! Siz yuborgan xabar: " + data);
        });
    }
}
