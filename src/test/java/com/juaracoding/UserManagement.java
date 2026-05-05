package com.juaracoding;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class UserManagement extends BaseTest {
    static String userId;
    @Test(priority = 1, description = "get all user")
    public void testGetUser() {
        Response response = adminRequest()
            .when()
                .get("/api/users")
            .then()
                .log().body()        // ← tambah ini untuk lihat response body
                .statusCode(200)   
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 2, description = "add data user")
    public void testAddUser() {
        String userName = "Suki" + System.currentTimeMillis(); // Unik
        String password = "password123";
        String email = "suki" + System.currentTimeMillis() + "@example.com";
        String fullName = "Suki ahmad";
        String roles = "USER"; // Bisa juga "ADMIN" tergantung kebutuhan


        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of(
                    "userName", userName,
                    "password", password,
                    "email", email,
                    "fullName", fullName,
                    "roles", roles
                ))
            .when()
                .post("/api/users")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200); 
        userId = response.jsonPath().getString("data.id");
        
        System.out.println("ID User yang didapat: " + userId);
        Assert.assertNotNull(userId, "Daftar user kosong, tidak bisa lanjut test Get By ID");            
    }

    @Test(priority = 3, description = "get data user by id")
    public void testGetUserById() {
        // Implementasi untuk mendapatkan user by ID
        Response response = adminRequest()
                .pathParam("id", userId)
            .when()
                .get("/api/users/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 4, description = "update data user")
    public void testUpdateUser() {
        String newFullName = "Suki Ahmad Updated";
        String newRoles = "USER,ADMIN"; // Contoh update role
        Response response = adminRequest()
                .contentType("application/json")
                .pathParam("id", userId)
                .body(Map.of(
                    "fullName", newFullName,
                    "roles", newRoles
                ))
            .when()
                .put("/api/users/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 5, description = "delete data user")
    public void testDeleteUser() {
        Response response = adminRequest()
                .pathParam("id", userId)
            .when()
                .delete("/api/users/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

}
