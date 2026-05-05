package com.juaracoding;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class cobaAuthorization {

    String adminToken;
    String adminRefreshToken;
    String userToken;
    String departmentId;
    String employeeId;

    // =============================================
    // SETUP — mirip setUp() di Reqres
    // =============================================

    @BeforeClass
    public void setUp() {
        System.out.println("set up");
        RestAssured.baseURI = "https://devops.juaracoding.com/hris";

        // Login Admin — dapat token
        Response adminResponse = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body(Map.of("email", "admin@ptdika.com", "password", "p4ssw0rd"))
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .response();

        adminToken = adminResponse.path("data.token");
        adminRefreshToken = adminResponse.path("data.refreshToken");
        System.out.println("admin token: " + adminToken);

        // // Login User — dapat token
        // Response userResponse = RestAssured
        //     .given()
        //         .header("Content-Type", "application/json")
        //         .body(Map.of("email", "user@ptdika.com", "password", "p4ssw0rd"))
        //     .when()
        //         .post("/api/auth/login")
        //     .then()
        //         .statusCode(200)
        //         .extract()
        //         .response();

        // userToken = userResponse.path("data.token");
        // System.out.println("user token: " + userToken);
    }

    // =============================================
    // DATA PROVIDER — mirip userData() di Reqres
    // =============================================

    // @DataProvider(name = "departmentData")
    // public Object[][] departmentData() {
    //     return new Object[][] {
    //         {"Engineering", "Software Engineering Team"},
    //         {"Marketing", "Marketing and Sales Team"},
    //         {"Finance", "Finance and Accounting Team"}
    //     };
    // }

    // =============================================
    // AUTHENTICATION TEST
    // =============================================

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

    // @Test(description = "login wrong password - 401", priority = 3)
    // public void testLoginWrongPassword() {
    //     System.out.println("login wrong password");
    //     RestAssured
    //         .given()
    //             .header("Content-Type", "application/json")
    //             .body(Map.of("email", "admin@ptdika.com", "password", "wrongpassword"))
    //         .when()
    //             .post("/api/auth/login")
    //         .then()
    //             .statusCode(401);
    //     System.out.println("correctly returned 401");
    // }

    @Test(description = "refresh token", priority = 2)
    public void testRefreshToken() {
        System.out.println("refresh token");
        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body(Map.of("refreshToken", adminRefreshToken))
            .when()
                .post("/api/auth/refresh-token")
            .then()
                .statusCode(200)
                .extract()
                .response();

        boolean success = response.path("success");
        System.out.println("success: " + success);
        Assert.assertTrue(success);
    }

    // // =============================================
    // // DEPARTMENT TEST — mirip CRUD di Reqres
    // // =============================================

    // @Test(description = "create department with data provider", dataProvider = "departmentData", priority = 5)
    // public void testCreateDepartment(String name, String description) {
    //     System.out.println("create department: " + name);
    //     Response response = RestAssured
    //         .given()
    //             .header("Authorization", "Bearer " + adminToken)
    //             .header("Content-Type", "application/json")
    //             .body(Map.of("name", name, "description", description))
    //         .when()
    //             .post("/api/departments")
    //         .then()
    //             .statusCode(200)
    //             .extract()
    //             .response();

    //     String createdName = response.path("data.name");
    //     String createdDesc = response.path("data.description");
    //     departmentId = response.path("data.id");
    //     System.out.println("name: " + createdName);
    //     System.out.println("description: " + createdDesc);
    //     Assert.assertEquals(createdName, name);
    //     Assert.assertEquals(createdDesc, description);
    // }

    // @Test(description = "get all departments", priority = 6)
    // public void testGetAllDepartments() {
    //     System.out.println("get all departments");
    //     Response response = RestAssured
    //         .given()
    //             .header("Authorization", "Bearer " + adminToken)
    //         .when()
    //             .get("/api/departments")
    //         .then()
    //             .statusCode(200)
    //             .extract()
    //             .response();

    //     boolean success = response.path("success");
    //     System.out.println("success: " + success);
    //     Assert.assertTrue(success);
    // }

    // @Test(description = "get department without token - 401", priority = 7)
    // public void testGetDepartmentWithoutToken() {
    //     System.out.println("get department without token");
    //     RestAssured
    //         .given()
    //         .when()
    //             .get("/api/departments")
    //         .then()
    //             .statusCode(401);
    //     System.out.println("correctly returned 401");
    // }

    // @Test(description = "update department", priority = 8)
    // public void testUpdateDepartment() {
    //     System.out.println("update department");
    //     String newName = "Engineering Updated";
    //     String newDesc = "Updated Description";
    //     Response response = RestAssured
    //         .given()
    //             .header("Authorization", "Bearer " + adminToken)
    //             .header("Content-Type", "application/json")
    //             .body(Map.of("name", newName, "description", newDesc))
    //         .when()
    //             .put("/api/departments/{id}", departmentId)
    //         .then()
    //             .statusCode(200)
    //             .extract()
    //             .response();

    //     boolean success = response.path("success");
    //     System.out.println("success: " + success);
    //     Assert.assertTrue(success);
    // }

    // @Test(description = "delete department", priority = 9)
    // public void testDeleteDepartment() {
    //     System.out.println("delete department");
    //     RestAssured
    //         .given()
    //             .header("Authorization", "Bearer " + adminToken)
    //         .when()
    //             .delete("/api/departments/{id}", departmentId)
    //         .then()
    //             .statusCode(200);
    //     System.out.println("department deleted");
    // }

}