package com.juaracoding;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class RoleManagement extends BaseTest {
    static String roleId;
    @Test(priority = 1, description = "get all role")
    public void testGetRole() {
        Response response = adminRequest()
            .when()
                .get("/api/roles")
            .then()
                .statusCode(200)
                .log().body()        // ← tambah ini untuk lihat response body
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 2, description = "Create role")
    public void testCreateRole() {
        String name = "ahmad suki" + System.currentTimeMillis();
        String description = "Role untuk testing API";
        String permissionId = "1"; // Ganti dengan ID permission yang valid
        Response response = adminRequest()
                .contentType("application/json")
                .body(Map.of(
                    "name", name,
                    "description", description,
                    "permissionIds", new String[]{permissionId} // Array of permission IDs
                ))
            .when()
                .post("/api/roles")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
        roleId = response.jsonPath().getString("data.id");
        
        System.out.println("ID Role yang didapat: " + roleId);
        Assert.assertNotNull(roleId, "Daftar role kosong, tidak bisa lanjut test Get By ID");
    }
    
    @Test(priority = 3, description = "get data role by id")
    public void testGetRoleById() {
        Response response = adminRequest()
                .pathParam("id", roleId)
            .when()
                .get("/api/roles/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 4, description = "update data role")
    public void testUpdateRole() {
        String newName = "ahmad suki updated" + System.currentTimeMillis();
        String newDescription = "Role untuk testing API - Updated";
        String permissionId = "1"; // Ganti dengan ID permission yang valid
        Response response = adminRequest()
                .contentType("application/json")
                .pathParam("id", roleId)
                .body(Map.of(
                    "name", newName,
                    "description", newDescription,
                    "permissionIds", new String[]{permissionId} // Array of permission IDs
                ))
            .when()
                .put("/api/roles/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Test(priority = 5, description = "delete data role")
    public void testDeleteRole() {        
        Response response = adminRequest()
                .pathParam("id", roleId)
            .when()
                .delete("/api/roles/{id}")
            .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.statusCode(), 200);
    }

}
