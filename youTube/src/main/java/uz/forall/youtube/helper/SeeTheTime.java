package uz.forall.youtube.helper;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SeeTheTime {
    public static String FOLDER_PATH = "";
    private static int morgenCloseHorse;
    private static int morgenCloseMin;
    private static int morgenCloseSec;
    private static int dayOpenHorse;
    private static int dayOpenMin;
    private static int dayOpenSec;
    private static int dayCloseHorse;
    private static int dayCloseMin;
    private static int dayCloseSec;
    private static int nightOpenHorse;
    private static int nightOpenMin;
    private static int nightOpenSec;

    // **Har kuni soat 00:00 da fayldan o‘qib oladi**
    @Scheduled(cron = "0 0 0 * * ?")
    public void reloadConfig() {
        System.out.println("Konfiguratsiya fayldan yangilanmoqda...");

        morgenCloseHorse = ConfigReader.readConfigFileToInt("morgenCloseHorse");
        morgenCloseMin = ConfigReader.readConfigFileToInt("morgenCloseMin");
        morgenCloseSec = ConfigReader.readConfigFileToInt("morgenCloseSec");

        dayOpenHorse = ConfigReader.readConfigFileToInt("dayOpenHorse");
        dayOpenMin = ConfigReader.readConfigFileToInt("dayOpenMin");
        dayOpenSec = ConfigReader.readConfigFileToInt("dayOpenSec");

        dayCloseHorse = ConfigReader.readConfigFileToInt("dayCloseHorse");
        dayCloseMin = ConfigReader.readConfigFileToInt("dayCloseMin");
        dayCloseSec = ConfigReader.readConfigFileToInt("dayCloseSec");

        nightOpenHorse = ConfigReader.readConfigFileToInt("nightOpenHorse");
        nightOpenMin = ConfigReader.readConfigFileToInt("nightOpenMin");
        nightOpenSec = ConfigReader.readConfigFileToInt("nightOpenSec");

        FOLDER_PATH = ConfigReader.readConfigFileToString("folder");

        System.out.println("Konfiguratsiya yangilandi!");
    }

    // **Dastur ilk marta ishga tushganda fayldan o‘qib oladi**
    @PostConstruct
    public void initConfig() {
        reloadConfig();
    }

    public static Boolean permissionVideo() {
        LocalTime now = LocalTime.now();

        if (
                (now.isAfter(LocalTime.of(morgenCloseHorse, morgenCloseMin, morgenCloseSec))
                        && now.isBefore(LocalTime.of(dayOpenHorse, dayOpenMin, dayOpenSec)))
                        ||
                        (now.isAfter(LocalTime.of(dayCloseHorse, dayCloseMin, dayCloseSec))
                                && now.isBefore(LocalTime.of(nightOpenHorse, nightOpenMin, nightOpenSec)))
        ) {
            return true;
        } else {
            return false;
        }
    }
}
