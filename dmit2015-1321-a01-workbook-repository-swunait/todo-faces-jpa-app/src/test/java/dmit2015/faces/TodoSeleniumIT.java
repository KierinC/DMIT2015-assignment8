package dmit2015.faces;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoSeleniumIT {

    private static WebDriver driver;

    static Long sharedEditId;

    @BeforeAll
    static void beforeAllTests() {
        WebDriverManager.chromedriver().setup();
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);

        // https://www.omgubuntu.co.uk/2022/04/how-to-install-firefox-deb-apt-ubuntu-22-04
//        WebDriverManager.firefoxdriver().setup();
//        driver = new FirefoxDriver();
    }

    @AfterAll
    static void afterAllTests() {
        driver.quit();
    }

    @BeforeEach
    void beforeEachTestMethod() {

    }

    @AfterEach
    void afterEachTestMethod() {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    private void setValue(String id, String value) {
        WebElement element = driver.findElement(By.id(id));
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.chord(Keys.BACK_SPACE));
        element.clear();
        element.sendKeys(value);
    }

    @Order(1)
    @ParameterizedTest
    // TODO Change the test data below
    @CsvSource(value = {
            "task,Buy Coffee,done,true",
            "task,Drink Coffee,done,false",
    })
    void shouldCreate(String taskField, String taskField1Value, String doneField, boolean doneFieldValue) {
        driver.get("http://localhost:8080/todos/create.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - Create");

        // Set the value for each form field
        setValue(taskField, taskField1Value);
//        setValue(field2Id, field2Value);
        if (doneFieldValue) {
            // Click on the checkbox for Done
            driver.findElement(By.id(doneField)).click();
        }

        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();
        // Find the create button and click on it
        driver.findElement(By.id("createButton")).click();

        // Wait for 3 seconds and verify navigate has been redirected to the listing page
        var wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var facesMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        // Verify the title of the page
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - List");
        // Verify the feedback message is displayed in the page
        String feedbackMessage = facesMessages.getText();
        assertThat(feedbackMessage)
                .containsIgnoringCase("Create was successful.");
        // The primary key of the entity is at the end of the feedback message after the period
        final int indexOfPrimaryKeyValue = feedbackMessage.indexOf(".") + 2;
        // Extract the primary key for re-use if we need to edit or delete the entity
        sharedEditId = Long.parseLong(feedbackMessage.substring(indexOfPrimaryKeyValue));
    }

    @Order(2)
    @ParameterizedTest
    // TODO Change the test data below
    @CsvSource({
            "Drink Coffee,false"
    })
    void shouldList(String expectedLastRowColumn1, String expectedLastRowColumn2) throws InterruptedException {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/todos/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - List");

        // Check the number of rows in the entity listing table
        int lastRow = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());

        // TODO: Compare the column values in the last row of the data table. You will need to change the row number of the last row
        // Define the XPATH for locating the each column of the last row
        final String lastRowFirstColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[2]", lastRow);
        final String lastRowSecondColumnColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[3]", lastRow);
//        final String lastThirdColumnColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[4]", lastRow);
        // Get the text for each column in the last row
        final String lastRowColumn1 = driver.findElement(By.xpath(lastRowFirstColumnXpathExpression)).getText();
        final String lastRowColumn2 = driver.findElement(By.xpath(lastRowSecondColumnColumnXpathExpression)).getText();
//        final String lastRowColumn3 = driver.findElement(By.xpath(lastThirdColumnColumnXpathExpression)).getText();

        // Verify each column of the last row
        assertThat(lastRowColumn1)
                .isEqualToIgnoringCase(expectedLastRowColumn1);
        assertThat(lastRowColumn2)
                .isEqualToIgnoringCase(expectedLastRowColumn2);
//        assertThat(lastRowColumn3)
//                .isEqualToIgnoringCase(expectedLastRowColumn3);

        // Verify that clicking on the edit link navigates to the Edit page
        driver.findElements(By.xpath("//a[contains(@id,'editLink')]")).get(0).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - Edit");
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Details page
        driver.findElements(By.xpath("//a[contains(@id,'detailsLink')]")).get(0).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - Details");
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Delete page
        driver.findElements(By.xpath("//a[contains(@id,'deleteLink')]")).get(0).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - Delete");
        Thread.sleep(3000);
        // Navigate back to the listing page
        driver.navigate().back();

    }

    @Order(3)
    @ParameterizedTest
    @CsvSource({
            "task,Drink Coffee,done,false",
    })
    void shouldDetails(String taskField, String taskFieldValue, String doneField, String doneFieldValue) {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/todos/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - List");

        int tableRowCount = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());
        int lastRowIndex = tableRowCount - 1;
        driver.findElements(By.xpath("//a[contains(@id,'detailsLink')]")).get(lastRowIndex).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - Details");

        // TODO: change the form field names and values you are expecting
        var actualField1Value = driver.findElement(By.id(taskField)).getText();
        var actualField2Value = driver.findElement(By.id(doneField)).getText();
        assertThat(actualField1Value)
                .isEqualToIgnoringCase(taskFieldValue);
        assertThat(actualField2Value)
                .isEqualToIgnoringCase(doneFieldValue);

    }

    @Order(4)
    @ParameterizedTest
    @CsvSource({
            "task,Drink Large Coffee,done,true",
    })
    void shouldEdit(String taskField, String taskFieldValue, String doneField, boolean doneFieldValue) {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/todos/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - List");

        int tableRowCount = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());
        int lastRowIndex = tableRowCount - 1;

        driver.findElements(By.xpath("//a[contains(@id,'editLink')]")).get(lastRowIndex).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - Edit");

        // Set the value for each form field
        setValue(taskField, taskFieldValue);
        // TODO write code to toggle Done checkbox

        driver.manage().window().maximize();
        driver.findElement(By.id("updateButton")).click();

        var wait = new WebDriverWait(driver, Duration.ofSeconds(11));
        var feedbackMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Update was successful.");
    }


    @Order(5)
    @Test
    void shouldDelete() {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/todos/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - List");

        int tableRowCount = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());
        int lastRowIndex = tableRowCount - 1;

        driver.findElements(By.xpath("//a[contains(@id,'deleteLink')]")).get(lastRowIndex).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - Delete");

        driver.findElement(By.id("deleteButton")).click();

        var wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        var yesConfirmationButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-confirmdialog-yes")));
        yesConfirmationButton.click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var feedbackMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Todo - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Delete was successful.");
    }

}