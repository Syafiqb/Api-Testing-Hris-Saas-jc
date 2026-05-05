package com.juaracoding;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class Employee extends BaseTest {

    static String employeeId;

    @Test(priority = 1, description = "get all employee")
    public void testGetEmployee() {
        Response response = adminRequest()
            .when()
                .get("/api/employees")
            .then()
                .statusCode(200)
                .log().body()        // ← tambah ini untuk lihat response body
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test (priority = 2, description = "add data employee")
    public void testAddEmployee() {
        String nip = "Hu6gmKbJwOWIJ9Nktpb7";
        String ktp = "6345704985664658";
        String fullName = "Suki" + System.currentTimeMillis();
        String email = "suki" + System.currentTimeMillis() + "@example.com";
        String phone = "081234567890";
        String departmentId = "1"; // Ganti dengan ID departemen yang valid
        String positionId = "1";   // Ganti dengan ID posisi yang valid
        String address = "Jl. Contoh No. 123, Jakarta";
        String createAcc = "true";

        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of(
                    "nip", nip,
                    "ktp", ktp,
                    "fullName", fullName,
                    "email", email,
                    "phone", phone,
                    "departmentId", departmentId,
                    "positionId", positionId,
                    "address", address,
                    "createAcc", createAcc
                ))
            .when()
                .post("/api/employees")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
        employeeId = response.jsonPath().getString("data.id");
        
        System.out.println("ID Employee yang didapat: " + employeeId);
        Assert.assertNotNull(employeeId, "Daftar employee kosong, tidak bisa lanjut test Get By ID"); 
    }

    @Test(priority = 3, description = "get data employee by id")
    public void testGetEmployeeById() {

        Response response = adminRequest()
                .pathParam("id", employeeId)
            .when()
                .get("/api/employees/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 4, description = "update data employee")
    public void testUpdateEmployee() {
        String newFullName = "Suki Updated";
        String newEmail = "suki.updated" + System.currentTimeMillis() + "@example.com";
        String newPhone = "081234567890";
        String newDepartmentId = "1"; // Ganti dengan ID departemen yang valid
        String newPositionId = "1";   // Ganti dengan ID posisi yang valid
        String newAddress = "Jl. Contoh No. 123, Jakarta";
        Response response = adminRequest()
                .contentType("application/json")
                .pathParam("id", employeeId)
                .body(Map.of(
                    "fullName", newFullName,
                    "email", newEmail,
                    "phone", newPhone,
                    "departmentId", newDepartmentId,
                    "positionId", newPositionId,
                    "address", newAddress
                ))
            .when()
                .put("/api/employees/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 5, description = "delete data employee")
    public void testDeleteEmployee() {
        Response response = adminRequest()
                .pathParam("id", employeeId)
            .when()
                .delete("/api/employees/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

}