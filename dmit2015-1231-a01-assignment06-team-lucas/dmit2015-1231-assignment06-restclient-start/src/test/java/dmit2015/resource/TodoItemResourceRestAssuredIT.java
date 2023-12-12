package dmit2015.resource;

import dmit2015.restclient.TodoItem;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://github.com/rest-assured/rest-assured
 * https://github.com/rest-assured/rest-assured/wiki/Usage
 * http://www.mastertheboss.com/jboss-frameworks/resteasy/restassured-tutorial
 * https://eclipse-ee4j.github.io/jsonb-api/docs/user-guide.html
 * https://github.com/FasterXML/jackson-databind
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoItemResourceRestAssuredIT {

    String todoResourceUrl = "http://localhost:8080/restapi/TodoItemsDto";
    String testDataResourceLocation;

    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "Create JAX-RS demo project,true,Create DTO version of TodoResource,false"
    })
    void shouldFindAll(String firstName, boolean firstComplete, String lastName, boolean lastComplete) {
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(todoResourceUrl)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String jsonBody = response.getBody().asString();

        Jsonb jsonb = JsonbBuilder.create();
        List<TodoItem> queryResultList = jsonb.fromJson(jsonBody, new ArrayList<TodoItem>(){}.getClass().getGenericSuperclass());

        TodoItem firstTodoItem = queryResultList.get(0);
        assertThat(firstTodoItem.getName())
                .isEqualTo(firstName);
        assertThat(firstTodoItem.isComplete())
                .isEqualTo(firstComplete);

        TodoItem lastTodoItem = queryResultList.get(queryResultList.size() - 1);
        assertThat(lastTodoItem.getName())
                .isEqualTo(lastName);
        assertThat(lastTodoItem.isComplete())
                .isEqualTo(lastComplete);

    }

    @Order(2)
    @Test
    void shouldCreate() {
        TodoItem newTodoItem = new TodoItem();
        newTodoItem.setName("Create REST Assured Integration Test");
        newTodoItem.setComplete(false);

        // Jsonb jsonb = JsonbBuilder.create();
        // String jsonBody = jsonb.toJson(newTodoItem);

        Response response = given()
                .contentType(ContentType.JSON)
                // .body(jsonBody)
                .body(newTodoItem)
                .when()
                .post(todoResourceUrl)
                .then()
                .statusCode(201)
                .extract()
                .response();
        testDataResourceLocation = response.getHeader("location");
    }

    @Order(3)
    @Test
    void shouldFineOne() {
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
        TodoItem existingTodoItem = jsonb.fromJson(jsonBody, TodoItem.class);

        assertThat(existingTodoItem.getName())
                .isEqualTo("Create REST Assured Integration Test");
        assertThat(existingTodoItem.isComplete())
                .isFalse();
    }

    @Order(4)
    @Test
    void shouldUpdate() {
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
        TodoItem existingTodoItem = jsonb.fromJson(jsonBody, TodoItem.class);

        existingTodoItem.setName("Updated Name");
        existingTodoItem.setComplete(true);

        String jsonTodoItem = jsonb.toJson(existingTodoItem);

        Response putResponse = given()
                .contentType(ContentType.JSON)
                .body(jsonTodoItem)
                .when()
                .put(testDataResourceLocation)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String putResponseJsonBody = putResponse.getBody().asString();
        TodoItem updatedTodoItem = jsonb.fromJson(putResponseJsonBody, TodoItem.class);

        assertThat(existingTodoItem)
                .usingRecursiveComparison()
//                .ignoringFields("updateTime")
                .isEqualTo(updatedTodoItem);
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