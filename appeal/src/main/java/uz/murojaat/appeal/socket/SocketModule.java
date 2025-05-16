package uz.murojaat.appeal.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.murojaat.appeal.model.Message;
import uz.murojaat.appeal.model.Organization;
import uz.murojaat.appeal.model.User;
import uz.murojaat.appeal.payload.ApiResult;
import uz.murojaat.appeal.payload.MessageDto;
import uz.murojaat.appeal.repository.MessageRepository;
import uz.murojaat.appeal.repository.OrganizationRepository;
import uz.murojaat.appeal.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SocketModule {


    private final SocketIOServer server;
    private final SocketService socketService;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final OrganizationRepository organizationRepository;


    //socketga bog'langan foydalanuvchilar username, socketId
    private final Map<Long, UUID> userSocketMap = new ConcurrentHashMap<>();


    public SocketModule(SocketIOServer server, SocketService socketService, UserRepository userRepository, MessageRepository messageRepository, OrganizationRepository organizationRepository) {
        this.server = server;
        this.socketService = socketService;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.organizationRepository = organizationRepository;
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
                } else {
                    User receiverUser = optionalUser.get();
                    userSocketMap.put(userId, client.getSessionId());
                    if (receiverUser.getIsOrderly()) {
                        Map<Organization, Integer> organizationIntegerMap = new HashMap<>();
                        Map<User, Integer> userIntegerMap = new HashMap<>();
                        for (Organization organization : organizationRepository.findAll()) {
                            List<User> userList = userRepository.findUserByOrganization_IdAndIsOrderly(organization.getId(), false);
                            int countOrganization = 0;
                            for (User user : userList) {
                                Integer countNotReceived = messageRepository.countByReceiverUser_IdAndSenderUser_IdAndIsRead(receiverUser.getId(), user.getId(), false);
                                if (countNotReceived > 0) {
                                    countOrganization++;
                                    userIntegerMap.put(user, countNotReceived);
                                }
                            }
                            organizationIntegerMap.put(organization, countOrganization);
                        }
                        server.getClient(client.getSessionId()).sendEvent("send_message", new ApiResult("success", true, organizationIntegerMap, userIntegerMap, null));
                    } else {
                        Map<User, Integer> userIntegerMap = new HashMap<>();
                        List<User> userByIsOrderly = userRepository.findUserByIsOrderly(true);
                        for (User user : userByIsOrderly) {
                            Integer countNotReceived = messageRepository.countByReceiverUser_IdAndSenderUser_IdAndIsRead(receiverUser.getId(), user.getId(), false);
                            userIntegerMap.put(user, countNotReceived);
                        }
                        server.getClient(client.getSessionId()).sendEvent("send_message", new ApiResult("success", true, userIntegerMap));
                    }
                }
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
