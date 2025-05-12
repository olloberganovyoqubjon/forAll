package uz.forall.murojaatsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class MurojaatSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MurojaatSocketApplication.class, args);
    }

}
