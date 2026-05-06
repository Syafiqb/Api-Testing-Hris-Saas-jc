package com.juaracoding;

import java.util.List;
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
        String username = "Suki" + System.currentTimeMillis(); // Unik
        String password = "password123";
        String email = "suki" + System.currentTimeMillis() + "@example.com";
        String fullName = "Suki ahmad";
        List<String> roles = List.of("ROLE_USER");  // ← List, bukan String
        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of(
                    "username", username,
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
        String username    = "SukiUpdated" + System.currentTimeMillis();
        String password    = "password123";
        String email       = "sukiupdated" + System.currentTimeMillis() + "@example.com";
        String fullName    = "Suki Ahmad Updated";
        List<String> roles = List.of("ROLE_USER");

        Response response = adminRequest()
                .pathParam("id", userId)
                .body(Map.of(
                        "username", username,   // ← wajib ada
                        "password", password,   // ← wajib ada
                        "email",    email,      // ← wajib ada
                        "fullName", fullName,
                        "roles",    roles
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
