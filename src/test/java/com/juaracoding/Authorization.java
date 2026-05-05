package com.juaracoding;

import java.util.Map;

import org.testng.Assert;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Authorization extends BaseTest {

    String adminToken;
    String adminRefreshToken;
    String userToken;
    String departmentId;
    String employeeId;

    // =============================================
    // SETUP — mirip setUp() di Reqres
    // =============================================

    // @BeforeClass
    // public void setUp() {
    //     System.out.println("set up");
    //     RestAssured.baseURI = "https://devops.juaracoding.com/hris";

    //     // Login Admin — dapat token
    //     Response adminResponse = RestAssured
    //         .given()
    //             .header("Content-Type", "application/json")
    //             .body(Map.of("email", "admin@ptdika.com", "password", "p4ssw0rd"))
    //         .when()
    //             .post("/api/auth/login")
    //         .then()
    //             .statusCode(200)
    //             .extract()
    //             .response();

    //     adminToken = adminResponse.path("data.token");
    //     adminRefreshToken = adminResponse.path("data.refreshToken");
    //     System.out.println("admin token: " + adminToken);

    //             // Login User — dapat token
    //     Response userResponse = RestAssured
    //         .given()
    //             .header("Content-Type", "application/json")
    //             .body(Map.of("email", "user@ptdika.com", "password", "p4ssw0rd"))
    //         .when()
    //             .post("/api/auth/login")
    //         .then()
    //             .statusCode(200)
    //             .extract()
    //             .response();

    //     userToken = userResponse.path("data.token");
    //     System.out.println("user token: " + userToken);
    // }

    @Test(description = "login admin success", priority = 1)
    public void testLoginAdmin() {
        System.out.println("login admin");
        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body(Map.of("email", "admin@ptdika.com", "password", "p4ssw0rd"))
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .response();

        boolean success = response.path("success");
        String message = response.path("message");
        System.out.println("success: " + success);
        System.out.println("message: " + message);
        Assert.assertTrue(success);
        Assert.assertEquals(message, "Login successful");
    }

    @Test(description = "login user success", priority = 2)
    public void testLoginUser() {
        System.out.println("login user");
        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body(Map.of("email", "user@ptdika.com", "password", "p4ssw0rd"))
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .response();

        boolean success = response.path("success");
        System.out.println("success: " + success);
        Assert.assertTrue(success);
    }
   

}