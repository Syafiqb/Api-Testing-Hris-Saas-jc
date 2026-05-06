package com.juaracoding;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class Employee extends BaseTest {

    static String employeeId;
    static String departmentId;
    static String positionId;

    @BeforeClass
    public void initData() {
    // Buat Department
    String deptName = "IT Department " + System.currentTimeMillis();
        Response deptResponse = adminRequest()
                .body(Map.of("name", deptName, "description", "Dept untuk testing"))
                .when()
                .post("/api/departments")
                .then()
                .statusCode(200)
                .extract().response();
        departmentId = deptResponse.jsonPath().getString("data.id");
        System.out.println("Department ID: " + departmentId);

        // Buat Position
        String posName = "UI Engineer " + System.currentTimeMillis();
        Response posResponse = adminRequest()
                .body(Map.of("name", posName, "description", "Posisi untuk testing"))
                .when()
                .post("/api/positions")
                .then()
                .statusCode(200)
                .extract().response();
        positionId = posResponse.jsonPath().getString("data.id");
        System.out.println("Position ID: " + positionId);
    }

    @AfterClass
    public void cleanData() {
        // Hapus Department
        adminRequest()
                .pathParam("id", departmentId)
                .when()
                .delete("/api/departments/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();

        // Hapus Position
        adminRequest()
                .pathParam("id", positionId)
                .when()
                .delete("/api/positions/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();
    }


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

    @Test(priority = 2, description = "add data employee")
    public void testAddEmployee() {
        long ts = System.currentTimeMillis();

        Response response = adminRequest()
                .body(Map.of(
                        "nip",           "NIP" + ts,
                        "ktp",           "123" + ts,
                        "fullName",      "Suki " + ts,
                        "email",         "suki" + ts + "@example.com",
                        "phone",         "081234567890",
                        "departmentId",  departmentId,
                        "positionId",    positionId,
                        "address",       "Jl. Contoh No. 123, Jakarta",
                        "createAccount", false
                ))
                .when()
                .post("/api/employees")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();

        employeeId = response.jsonPath().getString("data.id");
        Assert.assertNotNull(employeeId);
        System.out.println("ID Employee: " + employeeId);
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
        long ts = System.currentTimeMillis();

        Response response = adminRequest()
                .pathParam("id", employeeId)
                .body(Map.of(
                        "nip",           "NIP" + ts,
                        "ktp",           "123" + ts,
                        "fullName",      "Suki Updated",
                        "email",         "suki.updated" + ts + "@example.com",
                        "phone",         "081234567890",
                        "departmentId",  departmentId,
                        "positionId",    positionId,
                        "address",       "Jl. Contoh No. 123, Jakarta",
                        "createAccount", false
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