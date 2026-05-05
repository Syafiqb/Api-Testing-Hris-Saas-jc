package com.juaracoding;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class PermissionManagement extends BaseTest  {
    static String permissionId;
    @Test(priority = 1, description = "get all permission")
    public void testGetPermission() {
        Response response = adminRequest()
            .when()
                .get("/api/permissions")
            .then()
                .log().body()        // ← tambah ini untuk lihat response body
                .statusCode(200)   
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 2, description = "create permission")
    public void testCreatePermission() {
        String action = "CREATE_EMPLOYEE";
        String name = "Create Employee";
        String resource = "Employee";
        String description = "Izin untuk membuat data employee";
        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of(
                    "action", action,
                    "name", name,
                    "resource", resource,
                    "description", description
                ))
            .when()
                .post("/api/permissions")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);

        permissionId = response.jsonPath().getString("data.id");
        
        System.out.println("ID Permission yang didapat: " + permissionId);
        Assert.assertNotNull(permissionId, "Daftar permission kosong, tidak bisa lanjut test Get By ID");
    }

    @Test(priority = 3, description = "get data permission by id")
    public void testGetPermissionById() {
        Response response = adminRequest()
                .pathParam("id", permissionId)
            .when()
                .get("/api/permissions/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 4, description = "update data permission")
    public void testUpdatePermission() {
        String newAction = "UPDATE_EMPLOYEE";
        String newName = "Update Employee";
        String newResource = "Employee";
        String newDescription = "Izin untuk memperbarui data employee";
        Response response = adminRequest()
                .contentType("application/json")
                .pathParam("id", permissionId)
                .body(Map.of(
                    "action", newAction,
                    "name", newName,
                    "resource", newResource,
                    "description", newDescription
                ))
            .when()
                .put("/api/permissions/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 5, description = "delete data permission")
    public void testDeletePermission() {
        Response response = adminRequest()
                .pathParam("id", permissionId)
            .when()
                .delete("/api/permissions/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

}
