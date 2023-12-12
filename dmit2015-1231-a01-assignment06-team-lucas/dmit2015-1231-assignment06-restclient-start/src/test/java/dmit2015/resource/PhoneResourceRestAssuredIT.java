package dmit2015.resource;

import dmit2015.restclient.Phone;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.ejb.Local;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <a href="https://github.com/rest-assured/rest-assured">REST Assured GitHub repo</a>
 * <a href="https://github.com/rest-assured/rest-assured/wiki/Usage">REST Assured Usage</a>
 * <a href="http://www.mastertheboss.com/jboss-frameworks/resteasy/restassured-tutorial">REST Assured Tutorial</a>
 * <a href="https://github.com/FasterXML/jackson-databind">Jackson Data-Binding</a>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PhoneResourceRestAssuredIT {

    String phoneResourceUrl = "http://localhost:8080/restapi/PhonesDto";
    static String testDataResourceLocation;

    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "OnePlus 8,2020-10-10,OnePlus,899.99,Android 14"
    })
    void shouldCreate(String param1, LocalDate param2, String param3, BigDecimal param4, String param5) {
        Phone newPhone = new Phone();
        newPhone.setName(param1);
        newPhone.setDate(param2);
        newPhone.setBrand(param3);
        newPhone.setPrice(param4);
        newPhone.setOperatingSystem(param5);

        Jsonb jsonb = JsonbBuilder.create();
        String jsonBody = jsonb.toJson(newPhone);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(phoneResourceUrl)
                .then()
                .statusCode(201)
                .extract()
                .response();
        testDataResourceLocation = response.getHeader("location");
    }

    @Order(3)
    @ParameterizedTest
    @CsvSource(value = {
            "OnePlus 8,2020-10-10,OnePlus,899.99,Android 14"
    })
    void shouldFindOne(String param1, LocalDate param2, String param3, BigDecimal param4, String param5) {
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(testDataResourceLocation)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String jsonBody = response.getBody().asString();

        Jsonb jsonb = JsonbBuilder.create();
        Phone existingPhone = jsonb.fromJson(jsonBody, Phone.class);
        assertThat(existingPhone.getName())
                .isEqualTo(param1);
        assertThat(existingPhone.getDate())
                .isEqualTo(param2);
        assertThat(existingPhone.getBrand())
                .isEqualTo(param3);
        assertThat(existingPhone.getPrice())
                .isEqualTo(param4);
        assertThat(existingPhone.getOperatingSystem())
                .isEqualTo(param5);
    }

    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "OnePlus 7,2020-10-10,OnePlus,799.99,Android 13"
    })
    void shouldFindAll(String param1, LocalDate param2, String param3, BigDecimal param4, String param5) {
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(phoneResourceUrl)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String jsonBody = response.getBody().asString();

        Jsonb jsonb = JsonbBuilder.create();
        List<Phone> queryResultList = jsonb.fromJson(jsonBody, new ArrayList<Phone>() {
        }.getClass().getGenericSuperclass());

        Phone firstPhone = queryResultList.get(0);
        assertThat(firstPhone.getName())
                .isEqualTo(param1);
        assertThat(firstPhone.getDate())
                .isEqualTo(param2);
        assertThat(firstPhone.getBrand())
                .isEqualTo(param3);
        assertThat(firstPhone.getPrice())
                .isEqualTo(param4);
        assertThat(firstPhone.getOperatingSystem())
                .isEqualTo(param5);

    }

    @Order(4)
    @ParameterizedTest
    @CsvSource(value = {
            "OnePlus Best,2020-10-10,OnePlus,899.99,Android 14"
    })
    void shouldUpdate(String param1, LocalDate param2, String param3, BigDecimal param4, String param5) {
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(testDataResourceLocation)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String jsonBody = response.getBody().asString();

        Jsonb jsonb = JsonbBuilder.create();
        Phone existingPhone = jsonb.fromJson(jsonBody, Phone.class);
        existingPhone.setName(param1);
        existingPhone.setDate(param2);
        existingPhone.setBrand(param3);
        existingPhone.setPrice(param4);
        existingPhone.setOperatingSystem(param5);

        String jsonPhone = jsonb.toJson(existingPhone);

        Response putResponse = given()
                .contentType(ContentType.JSON)
                .body(jsonPhone)
                .when()
                .put(testDataResourceLocation)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String putResponseJsonBody = putResponse.getBody().asString();
        Phone updatedPhone = jsonb.fromJson(putResponseJsonBody, Phone.class);

        assertThat(existingPhone)
                .usingRecursiveComparison()
//                .ignoringFields("updateTime")
                .isEqualTo(updatedPhone);
    }

    @Order(5)
    @Test
    void shouldDelete() {
        given()
                .when()
                .delete(testDataResourceLocation)
                .then()
                .statusCode(204);
    }

}