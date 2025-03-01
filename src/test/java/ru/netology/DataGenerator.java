package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker FAKER = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static RegistrationDto sendRequest(RegistrationDto user) {
        given()
                .baseUri("http://localhost:9999")// "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user)// передаём в теле объект, который будет преобразован в JSON

                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос

                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
        return user;
    }

    public static String getRandomLogin() {
        return FAKER.name().username();
    }

    public static String getRandomPassword() {
        return FAKER.internet().password();
    }

    public static class Registration {
        private Registration() {

        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) {
            return sendRequest(getUser(status));
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;

    }

}
