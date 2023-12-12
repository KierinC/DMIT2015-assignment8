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
public class LocationSeleniumIT {

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
            "streetAddress,354 2nd Street,postalCode,V3A 1H7,city,Vancouver,stateProvince,British Columbia,selectedCountryId,selectedCountryId_5",
    })
    void shouldCreate(String field1Id, String field1Value, String field2Id, String field2Value, String field3Id, String field3Value, String field4Id, String field4Value, String field5Id, String field5Value) {
        driver.get("http://localhost:8080/locations/create.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - Create");

        // Set the value for each form field
        setValue(field1Id, field1Value);
        setValue(field2Id, field2Value);
        setValue(field3Id, field3Value);
        setValue(field4Id, field4Value);
        // Select the value for the country field with clicks
        driver.findElement(By.id(field5Id)).click();
        driver.findElement(By.id(field5Value)).click();

        // Maximize the browser window, so we can see the data being inputted
        driver.manage().window().maximize();
        // Find the create button and click on it
        driver.findElement(By.id("createButton")).click();

        // Wait for 3 seconds and verify navigate has been redirected to the listing page
        var wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var facesMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        // Verify the title of the page
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - List");
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
            //"354 2nd Street,V3A 1H7,Vancouver,British Columbia,Canada",
            //"Mariano Escobedo 9991,11932,Mexico City,Distrito Federal,Mexico",
            "1297 Via Cola di Rie,00989,Roma,'',Italy",
    })
    void shouldList(String expectedLastRowColumn1, String expectedLastRowColumn2, String expectedLastRowColumn3, String expectedLastRowColumn4, String expectedLastRowColumn5) {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/locations/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - List");

        // Maximize window
        driver.manage().window().maximize();

        // Go to page 3 of the entity listing table
        //driver.findElement(By.xpath("//div[@id='entityTable_paginator_bottom']/span[@class='ui-paginator-pages']/a[contains(text(),'3')]")).click();

        // Check the number of rows in the entity listing table
        int lastRow = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());

        // TODO: Compare the column values in the last row of the data table. You will need to change the row number of the last row
        // Define the XPATH for locating the each column of the last row
//        final String lastRowFirstColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[2]", lastRow);
//        final String lastRowSecondColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[3]", lastRow);
//        final String lastRowThirdColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[4]", lastRow);
//        final String lastRowFourthColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[5]", lastRow);
//        final String lastRowFifthColumnXpathExpression = String.format("//table[@role='grid']/tbody/tr[%d]/td[6]", lastRow);
        // Get the text for each column in the first row
        final String lastRowFirstColumnXpathExpression = "//table[@role='grid']/tbody/tr[1]/td[2]";
        final String lastRowSecondColumnXpathExpression = "//table[@role='grid']/tbody/tr[1]/td[3]";
        final String lastRowThirdColumnXpathExpression = "//table[@role='grid']/tbody/tr[1]/td[4]";
        final String lastRowFourthColumnXpathExpression = "//table[@role='grid']/tbody/tr[1]/td[5]";
        final String lastRowFifthColumnXpathExpression = "//table[@role='grid']/tbody/tr[1]/td[6]";
        // Get the text for each column in the last row
        final String lastRowColumn1 = driver.findElement(By.xpath(lastRowFirstColumnXpathExpression)).getText();
        final String lastRowColumn2 = driver.findElement(By.xpath(lastRowSecondColumnXpathExpression)).getText();
        final String lastRowColumn3 = driver.findElement(By.xpath(lastRowThirdColumnXpathExpression)).getText();
        final String lastRowColumn4 = driver.findElement(By.xpath(lastRowFourthColumnXpathExpression)).getText();
        final String lastRowColumn5 = driver.findElement(By.xpath(lastRowFifthColumnXpathExpression)).getText();

        // Verify each column of the last row
        assertThat(lastRowColumn1)
                .isEqualToIgnoringCase(expectedLastRowColumn1);
        assertThat(lastRowColumn2)
                .isEqualToIgnoringCase(expectedLastRowColumn2);
        assertThat(lastRowColumn3)
                .isEqualToIgnoringCase(expectedLastRowColumn3);
        assertThat(lastRowColumn4)
                .isEqualToIgnoringCase(expectedLastRowColumn4);
        assertThat(lastRowColumn5)
                .isEqualToIgnoringCase(expectedLastRowColumn5);


        // Verify that clicking on the edit link navigates to the Edit page
        driver.findElements(By.xpath("//a[contains(@id,'editLink')]")).get(0).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - Edit");
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Details page
        driver.findElements(By.xpath("//a[contains(@id,'detailsLink')]")).get(0).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - Details");
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Delete page
        driver.findElements(By.xpath("//a[contains(@id,'deleteLink')]")).get(0).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - Delete");
        // Navigate back to the listing page
        driver.navigate().back();
    }

    @Order(3)
    @ParameterizedTest
    @CsvSource({
            //"streetAddress,354 2nd Street,postalCode,V3A 1H7,city,Vancouver,stateProvince,British Columbia,countryName,Canada",
            "streetAddress, Mariano Escobedo 9991,postalCode,11932,city,Mexico City,stateProvince,Distrito Federal,countryName,Mexico",
    })
    void shouldDetails(String field1Id, String field1Value, String field2Id, String field2Value, String field3Id, String field3Value, String field4Id, String field4Value, String field5Id, String field5Value) {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/locations/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - List");

        // Maximize window
        driver.manage().window().maximize();

        // Go to page 3 of the entity listing table
        driver.findElement(By.xpath("//div[@id='entityTable_paginator_bottom']/span[@class='ui-paginator-pages']/a[contains(text(),'3')]")).click();

        int tableRowCount = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());
        int lastRowIndex = tableRowCount - 1;
        driver.findElements(By.xpath("//a[contains(@id,'detailsLink')]")).get(lastRowIndex).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - Details");

        // TODO: change the form field names and values you are expecting
        var actualField1Value = driver.findElement(By.id(field1Id)).getText();
        var actualField2Value = driver.findElement(By.id(field2Id)).getText();
        var actualField3Value = driver.findElement(By.id(field3Id)).getText();
        var actualField4Value = driver.findElement(By.id(field4Id)).getText();
        var actualField5Value = driver.findElement(By.id(field5Id)).getText();

        assertThat(actualField1Value)
                .isEqualToIgnoringCase(field1Value);
        assertThat(actualField2Value)
                .isEqualToIgnoringCase(field2Value);
        assertThat(actualField3Value)
                .isEqualToIgnoringCase(field3Value);
        assertThat(actualField4Value)
                .isEqualToIgnoringCase(field4Value);
        assertThat(actualField5Value)
                .isEqualToIgnoringCase(field5Value);

    }

    @Order(4)
    @ParameterizedTest
    @CsvSource({
            "streetAddress,700 3rd Street,postalCode,T4B 2H9,city,Edmonton,stateProvince,Alberta,selectedCountryId,selectedCountryId_4",
    })
    void shouldEdit(String field1Id, String field1Value, String field2Id, String field2Value, String field3Id, String field3Value, String field4Id, String field4Value, String field5Id, String field5Value) {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/locations/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - List");

        int tableRowCount = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());
        int lastRowIndex = tableRowCount - 1;

        driver.findElements(By.xpath("//a[contains(@id,'editLink')]")).get(lastRowIndex).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - Edit");

        // Set the value for each form field
        setValue(field1Id, field1Value);
        setValue(field2Id, field2Value);
        setValue(field3Id, field3Value);
        setValue(field4Id, field4Value);
        // Select the value for the country field with clicks
        driver.findElement(By.id(field5Id)).click();
        driver.findElement(By.id(field5Value)).click();


        driver.manage().window().maximize();
        driver.findElement(By.id("updateButton")).click();

        var wait = new WebDriverWait(driver, Duration.ofSeconds(11));
        var feedbackMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Update was successful.");
    }


    @Order(5)
    @Test
    void shouldDelete() {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/locations/index.xhtml");
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - List");

        int tableRowCount = (driver.findElements(By.xpath("//table[@role='grid']/tbody/tr")).size());
        int lastRowIndex = tableRowCount - 1;

        driver.findElements(By.xpath("//a[contains(@id,'deleteLink')]")).get(lastRowIndex).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - Delete");

        driver.findElement(By.id("deleteButton")).click();

        var wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        var yesConfirmationButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-confirmdialog-yes")));
        yesConfirmationButton.click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var feedbackMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Location - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Delete was successful.");
    }

}