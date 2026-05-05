package com.juaracoding;


 
import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
 
import static io.restassured.RestAssured.given;

import java.util.Map;
 
public class BaseTest {
 
    Dotenv dotenv = Dotenv.load();

 
    String emailAdmin;
    String password;
 
    protected static String adminToken;
    protected static String adminRefreshToken;
    protected static String userToken;
    protected static String userRefreshToken;
 
    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = "https://devops.juaracoding.com/hris";
        
        emailAdmin = dotenv.get("EMAILADMIN", "admin@ptdika.com");
        password = dotenv.get("PASSWORD", "p4ssw0rd");
 
 
        Response adminResponse = 
            given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .body(Map.of("email", emailAdmin, "password", password))
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
 
        adminToken        = adminResponse.jsonPath().getString("data.token");
        adminRefreshToken = adminResponse.jsonPath().getString("data.refreshToken");
 
        System.out.println("=== Login sukses, token didapat ===");

        // Login User — dapat token
        Response userResponse = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body(Map.of("email", "user@ptdika.com", "password", "p4ssw0rd"))
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .response();

        userToken = userResponse.path("data.token");
        userRefreshToken = userResponse.path("data.refreshToken");
        System.out.println("user token: " + userToken);
    }
 
    protected RequestSpecification adminRequest() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken);
    }

    protected RequestSpecification userRequest() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken);
    }
 
    protected RequestSpecification noAuthRequest() {
        return given()
                .contentType(ContentType.JSON);
    }
}
