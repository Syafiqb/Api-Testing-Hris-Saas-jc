package com.juaracoding;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UserProfile extends BaseTest {



    @Test(priority = 1, description = "Get profile admin - berhasil")
    public void testGetProfileAdmin() {
        Response response = adminRequest()
            .when()
                .get("/api/profile")
            .then()
                .statusCode(200)
                .log().body()        // ← tambah ini
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
    }

     @Test(priority = 2, description = "Get profile user biasa - berhasil")
    public void testGetProfileUser() {
        Response response = userRequest()
            .when()
                .get("/api/profile")
            .then()
                .statusCode(200)
                .log().body()        // ← tambah ini
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 3, description = "Get profile tanpa token - harus 401/403")
    public void testGetProfileUnauthorized() {
        Response response = noAuthRequest()
            .when()
                .get("/api/profile")
            .then()
                .log().body()        // ← tambah ini
                .extract().response();
 
        Assert.assertTrue(response.statusCode() == 401 || response.statusCode() == 403);
        System.out.println("[PASS] Get profile tanpa token - status: " + response.statusCode());
    }

    @Test(priority = 4, description = "Update profile admin - berhasil")
    public void testUpdateProfileAdmin() {
        String newName  = "Admin Updated";
        String newPhone = "081234567890";
        String address  = "Jl. Contoh No. 123, Jakarta";

        // Siapkan file foto (taruh file di src/test/resources/)
        File photo = new File("src/test/resources/ambatukam.jpeg");

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("multipart/form-data")
                .multiPart("fullName", newName)
                .multiPart("phone",    newPhone)
                .multiPart("address",  address)
                .multiPart("photo",    photo, "image/jpeg")  
                .when()
                .put("/api/profile")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
    }


}


