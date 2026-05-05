package com.juaracoding;

import io.restassured.response.Response;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Department extends BaseTest {

    static String departmentId;

    @Test(priority = 1)
    public void testGetAllDepartments() {
        // Menggunakan adminRequest() yang sudah ada token-nya di BaseTest
        Response response = adminRequest()
            .when()
                .get("/api/departments")
            .then()
                .statusCode(200)
                .log().body()        // ← tambah ini untuk lihat response body
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 2, description = "add data department")
    public void testAddDepartment() {
        System.out.println("add data department");
        String name        = "IT Department" + System.currentTimeMillis(); // ← unik
        String description = "Menjadi pengendali teknologi informasi di perusahaan";
        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of("name", name, "description", description))
            .when()
                .post("/api/departments")
            .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);

        // Ambil ID pertama dari list untuk dipake di test berikutnya
        departmentId = response.jsonPath().getString("data.id");
        
        System.out.println("ID Departemen yang didapat: " + departmentId);
        Assert.assertNotNull(departmentId, "Daftar departemen kosong, tidak bisa lanjut test Get By ID");
    }

    @Test(priority = 3,description = "get data department by id")
    public void testGetDepartmentById() {
        Response response = adminRequest()
                .pathParam("id", departmentId)
            .when()
                .get("/api/departments/{id}")
            .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        String actualId = response.jsonPath().getString("data.id");
        Assert.assertEquals(actualId, departmentId);
    }

    @Test(priority = 4, description = "update data department")
    public void testUpdateDepartment() {
        String newName = "IT Department Updated"+ System.currentTimeMillis();
        String newDescription = "Deskripsi baru untuk IT Department";
        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of("name", newName, "description", newDescription))
                .pathParam("id", departmentId)
            .when()
                .put("/api/departments/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 5, description = "delete data department")
    public void testDeleteDepartment() {
        Response response = adminRequest()
                .pathParam("id", departmentId)
            .when()
                .delete("/api/departments/{id}")
            .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }
}