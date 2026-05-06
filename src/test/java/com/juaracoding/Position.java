package com.juaracoding;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class Position extends BaseTest {

    static String positionId;

     @Test(priority = 1, description = "get all position")
    public void testGetPosition() {
        Response response = adminRequest()
            .when()
                .get("/api/positions")
            .then()
                .log().body()        // ← tambah ini untuk lihat response body
                .statusCode(200)   
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 2, description = "add data position")
    public void testAddPosition() {
        String name = "UI Engineer" + System.currentTimeMillis(); // Unik
        String description = "Bertanggung jawab untuk mengendalikan elemen visual dan interaktif pada aplikasi";
        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of("name", name, "description", description))
            .when()
                .post("/api/positions")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();

        // Ambil ID pertama dari list untuk dipake di test berikutnya
        positionId = response.jsonPath().getString("data.id");
        
        System.out.println("ID Position yang didapat: " + positionId);
        Assert.assertNotNull(positionId, "Daftar position kosong, tidak bisa lanjut test Get By ID");
    }

    @Test(priority = 3, description = "get data position by id")
    public void testGetPositionById() {
        Response response = adminRequest()
                .pathParam("id", positionId)
            .when()
                .get("/api/positions/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 4, description = "update data position")
    public void testUpdatePosition() {
        String newName = "UI Engineer Updated" + System.currentTimeMillis();
        String newDescription = "Bertanggung jawab untuk mengendalikan elemen visual dan interaktif pada aplikasi, termasuk pembaruan terbaru.";
        Response response = adminRequest()
                .contentType("application/json")
                .pathParam("id", positionId)
                .body(Map.of("name", newName, "description", newDescription))
            .when()
                .put("/api/positions/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 5, description = "delete data position")
    public void testDeletePosition() {
        Response response = adminRequest()
                .pathParam("id", positionId)
            .when()
                .delete("/api/positions/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }
}
