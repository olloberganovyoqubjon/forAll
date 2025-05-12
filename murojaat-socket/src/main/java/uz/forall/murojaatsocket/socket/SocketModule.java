package uz.forall.murojaatsocket.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.forall.murojaatsocket.model.User;
import uz.forall.murojaatsocket.payload.ApiResult;
import uz.forall.murojaatsocket.payload.MessageDto;
import uz.forall.murojaatsocket.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SocketModule {


    private final SocketIOServer server;
    private final SocketService socketService;
    private final UserRepository userRepository;


    //socketga bog'langan foydalanuvchilar username, socketId
    private final Map<Long, UUID> userSocketMap = new ConcurrentHashMap<>();


    public SocketModule(SocketIOServer server, SocketService socketService, UserRepository userRepository) {
        this.server = server;
        this.socketService = socketService;
        this.userRepository = userRepository;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", MessageDto.class, onChatReceived());

    }


    /**
     * xabar jo'natish
     * MessageDto (content, senderUserId, receiverUserId) keladi
     *
     * @return DataListener qaytadi
     */
    private DataListener<MessageDto> onChatReceived() {
        return (senderClient, data, ackSender) -> {
//            qabul qiluvchi id raqami
            Optional<User> optionalReceiverUser = userRepository.findById(data.getReceiverUserId());
            Optional<User> optionalSenderUser = userRepository.findById(data.getSenderUserId());
            if (optionalSenderUser.isEmpty() || optionalReceiverUser.isEmpty()) {
                server.getClient(senderClient.getSessionId()).sendEvent("error", new ApiResult("Bunday foydalanuvchi mavjud emas!", false));
            }
            UUID receiverSocketId = userSocketMap.get(data.getReceiverUserId());
//            agar foydalanuvchi online bo'lmasa receiverSocketId null ga teng bo'ladi, aks holda u online bo'ladi
            boolean saveMessage;
            if (receiverSocketId == null) {
                saveMessage = socketService.saveMessage(data, false);
                if (!saveMessage) {
                    server.getClient(senderClient.getSessionId()).sendEvent("error", new ApiResult("Saqlashda muammo tug'uldi!", false));
                }
            } else {
                saveMessage = socketService.saveMessage(data, true);
                if (!saveMessage) {
                    server.getClient(senderClient.getSessionId()).sendEvent("error", new ApiResult("Saqlashda muammo tug'uldi!", false));
                } else {
                    server.getClient(receiverSocketId).sendEvent("send_message", new ApiResult("success", true, data));
                }
            }
        };
    }


    /**
     * socketga bog'lanish
     *
     * @return ConnectListener
     */
    private ConnectListener onConnected() {
        return (client) -> {
            List<String> userIdList = client.getHandshakeData().getUrlParams().get("userId");
            if (userIdList == null || userIdList.isEmpty()) {
                log.warn("userId not provided in handshake");
                return;
            }
            try {
                Long userId = Long.valueOf(userIdList.get(0));
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isEmpty()) {
                    log.warn("user not found");
                }
                userSocketMap.put(userId, client.getSessionId());
            } catch (NumberFormatException e) {
                log.error("Invalid userId format: {}", userIdList.get(0));
            }
        };

    }


    /**
     * socketdan chiqish
     *
     * @return DisconnectListener
     */
    private DisconnectListener onDisconnected() {
        return client -> {
            List<String> userIdList = client.getHandshakeData().getUrlParams().get("userId");
            if (userIdList == null || userIdList.isEmpty()) {
                log.warn("userId not provided in handshake");
                return;
            }
            try {
                Long userId = Long.valueOf(userIdList.get(0));
                userSocketMap.remove(userId);
            } catch (NumberFormatException e) {
                log.error("Invalid userId format: {}", userIdList.get(0));
            }
        };
    }
}
