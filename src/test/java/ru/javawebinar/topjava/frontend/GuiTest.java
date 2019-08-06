package ru.javawebinar.topjava.frontend;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

//should to run in manual only after app was deployed
@Disabled
class GuiTest {

    private static final String PATH_TO_CHROMEDRIVER_EXE = "C:\\_DEV\\selenium_tests\\drivers\\chromedriver.exe";
    private static ChromeDriverService service;
    private static WebDriver driver;

    @BeforeAll()
    static void createAndStartService() throws Exception {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(PATH_TO_CHROMEDRIVER_EXE))
                .usingAnyFreePort()
                .build();
        service.start();
    }

    @BeforeEach()
    void setUp() {
        driver = new ChromeDriver(service);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @AfterAll
    static void createAndStopService() {
        service.stop();
    }

    @Test
    void testLogin() throws Exception {
        loginAsUser();
        String title = driver.getTitle();
        assertEquals(title, "Подсчет калорий");
    }

    private void loginAsUser() {
        driver.get("http://localhost:8080/topjava/login");
        WebElement loginField = driver.findElement(By.name("username"));
        loginField.sendKeys("user@yandex.ru");
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.submit();
    }
}
