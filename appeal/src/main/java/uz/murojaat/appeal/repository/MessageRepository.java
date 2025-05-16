package uz.murojaat.appeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.murojaat.appeal.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderUser_IdAndReceiverUser_Id(Long senderUserId, Long receiverUserId);
    List<Message> findBySenderUser_IdAndReceiverUser_IdAndIsRead(Long senderUserId, Long receiverUserId, Boolean isRead);

    List<Message> findByReceiverUser_IdAndIsRead(Long receiverUserId, Boolean isRead);

    Integer countByReceiverUser_IdAndSenderUser_IdAndIsRead(Long receiverUserId, Long senderUserId, Boolean isRead);


}
