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
    protected static String adminToken;
    protected static String adminRefreshToken;
    protected static String userToken;
    protected static String userRefreshToken;

    Dotenv dotenv;

    @BeforeSuite
    public void setup() {

        dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        RestAssured.baseURI = "https://devops.juaracoding.com/hris";

        String emailAdmin = dotenv.get("EMAILADMIN", "admin@ptdika.com");
        String password = dotenv.get("PASSWORD", "p4ssw0rd");

        Response adminResponse = given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", emailAdmin, "password", password))
                .post("/api/auth/login");

        if (adminResponse.statusCode() == 200) {
            adminToken = adminResponse.jsonPath().getString("data.token");
        } else {
            throw new RuntimeException("Admin login gagal");
        }
    
        Response userResponse = given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "user@ptdika.com", "password", "p4ssw0rd"))
                .post("/api/auth/login");

        if (userResponse.statusCode() == 200) {
            userToken = userResponse.jsonPath().getString("data.token");
            userRefreshToken = userResponse.jsonPath().getString("data.refreshToken");
        } else {
            throw new RuntimeException("User login gagal");
        }

        System.out.println("USER TOKEN = " + userToken);
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
