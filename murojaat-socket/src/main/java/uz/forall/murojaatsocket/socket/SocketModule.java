package uz.forall.murojaatsocket.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import uz.forall.murojaatsocket.constants.Constants;
import uz.forall.murojaatsocket.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class SocketModule {


    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());

    }


    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.saveMessage(senderClient, data);
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {
//            String room = client.getHandshakeData().getSingleUrlParam("room");
//            String username = client.getHandshakeData().getSingleUrlParam("room");
            var params = client.getHandshakeData().getUrlParams();
            String room = String.join("", params.get("room"));
            String username = String.join("", params.get("username"));
            client.joinRoom(room);
            socketService.saveInfoMessage(client, username + Constants.WELCOME_MESSAGE, room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = String.join("", params.get("room"));
            String username = String.join("", params.get("username"));
            socketService.saveInfoMessage(client, username + Constants.DISCONNECT_MESSAGE, room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }


}
