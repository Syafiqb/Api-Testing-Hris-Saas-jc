package com.juaracoding;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HrisApiTest {

    String adminToken;
    String userToken;

    String departmentId;
    String employeeId;
    String permissionId;
    String positionId;
    String userId;
    String roleId;

    // =============================================
    // SETUP — Login dan simpan token
    // =============================================

    @BeforeClass
    public void setUp() {
        System.out.println("=== SET UP ===");
        RestAssured.baseURI = "https://devops.juaracoding.com/hris";

        // Login Admin
        Response adminRes = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"admin@ptdika.com\", \"password\": \"p4ssw0rd\" }")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
        adminToken = adminRes.path("data.token");
        System.out.println("Admin login OK");

        // Login User
        Response userRes = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"user@ptdika.com\", \"password\": \"p4ssw0rd\" }")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
        userToken = userRes.path("data.token");
        System.out.println("User login OK");
    }

    // =============================================
    // 1. AUTHENTICATION
    // =============================================

    @Test(description = "[AUTH] Login admin - success", priority = 1)
    public void testLoginAdmin() {
        System.out.println("--- [AUTH] Login Admin ---");
        Response res = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"admin@ptdika.com\", \"password\": \"p4ssw0rd\" }")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
        boolean success = res.path("success");
        String message = res.path("message");
        System.out.println("Message: " + message);
        Assert.assertTrue(success);
        Assert.assertEquals(message, "Login successful");
    }

    @Test(description = "[AUTH] Login user - success", priority = 2)
    public void testLoginUser() {
        System.out.println("--- [AUTH] Login User ---");
        Response res = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"user@ptdika.com\", \"password\": \"p4ssw0rd\" }")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
        boolean success = res.path("success");
        Assert.assertTrue(success);
    }

    @Test(description = "[AUTH] Login - wrong password (negative)", priority = 3)
    public void testLoginWrongPassword() {
        System.out.println("--- [AUTH] Login Wrong Password ---");
        RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"admin@ptdika.com\", \"password\": \"wrongpassword\" }")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(401);
        System.out.println("Correctly returned 401");
    }

    @Test(description = "[AUTH] Refresh token - success", priority = 4)
    public void testRefreshToken() {
        System.out.println("--- [AUTH] Refresh Token ---");
        Response loginRes = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"admin@ptdika.com\", \"password\": \"p4ssw0rd\" }")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
        String refreshToken = loginRes.path("data.refreshToken");

        Response res = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + refreshToken)
            .when()
                .post("/api/auth/refresh-token")
            .then()
                .extract().response();
        System.out.println("Refresh token status: " + res.statusCode());
        System.out.println("Response: " + res.asString());
    }

    @Test(description = "[AUTH] Logout - success", priority = 5)
    public void testLogout() {
        System.out.println("--- [AUTH] Logout ---");
        Response loginRes = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"user@ptdika.com\", \"password\": \"p4ssw0rd\" }")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
        String tempToken = loginRes.path("data.token");

        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + tempToken)
                .header("Content-Type", "application/json")
            .when()
                .post("/api/auth/logout")
            .then()
                .extract().response();
        System.out.println("Logout status: " + res.statusCode());
        System.out.println("Response: " + res.asString());
    }

    // =============================================
    // 2. DEPARTMENT
    // =============================================

    @Test(description = "[DEPT] Create department - success", priority = 10)
    public void testCreateDepartment() {
        System.out.println("--- [DEPT] Create Department ---");
        String body = "{ \"name\": \"IT Department\", \"description\": \"Information Technology\" }";
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .post("/api/departments")
            .then()
                .statusCode(200)
                .extract().response();
        boolean success = res.path("success");
        departmentId = res.path("data.id");
        String name = res.path("data.name");
        System.out.println("Department ID: " + departmentId + " | Name: " + name);
        Assert.assertTrue(success);
        Assert.assertEquals(name, "IT Department");
    }

    @Test(description = "[DEPT] Get all departments - success", priority = 11)
    public void testGetAllDepartments() {
        System.out.println("--- [DEPT] Get All Departments ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/departments")
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[DEPT] Get department by ID - success", priority = 12)
    public void testGetDepartmentById() {
        System.out.println("--- [DEPT] Get Department By ID ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/departments/" + departmentId)
            .then()
                .statusCode(200)
                .extract().response();
        System.out.println("Name: " + res.path("data.name").toString());
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[DEPT] Update department - success", priority = 13)
    public void testUpdateDepartment() {
        System.out.println("--- [DEPT] Update Department ---");
        String body = "{ \"name\": \"IT Department Updated\", \"description\": \"Updated\" }";
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .put("/api/departments/" + departmentId)
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[DEPT] Delete department - success", priority = 14)
    public void testDeleteDepartment() {
        System.out.println("--- [DEPT] Delete Department ---");
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .delete("/api/departments/" + departmentId)
            .then()
                .statusCode(200);
        System.out.println("Department deleted");
    }

    @Test(description = "[DEPT] Access without token (negative)", priority = 15)
    public void testDepartmentNoToken() {
        System.out.println("--- [DEPT] No Token ---");
        RestAssured.given().when().get("/api/departments").then().statusCode(401);
        System.out.println("Correctly returned 401");
    }

    // =============================================
    // 3. POSITION
    // =============================================

    @Test(description = "[POS] Create position - success", priority = 20)
    public void testCreatePosition() {
        System.out.println("--- [POS] Create Position ---");
        String body = "{ \"name\": \"Software Engineer\", \"description\": \"Backend Developer\" }";
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .post("/api/positions")
            .then()
                .extract().response();
        System.out.println("Status: " + res.statusCode() + " | Response: " + res.asString());
        positionId = res.path("data.id");
    }

    @Test(description = "[POS] Get all positions - success", priority = 21)
    public void testGetAllPositions() {
        System.out.println("--- [POS] Get All Positions ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/positions")
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[POS] Get position by ID - success", priority = 22)
    public void testGetPositionById() {
        System.out.println("--- [POS] Get Position By ID ---");
        if (positionId == null) { System.out.println("SKIP - ID null"); return; }
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/positions/" + positionId)
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[POS] Update position - success", priority = 23)
    public void testUpdatePosition() {
        System.out.println("--- [POS] Update Position ---");
        if (positionId == null) { System.out.println("SKIP - ID null"); return; }
        String body = "{ \"name\": \"Senior Engineer\", \"description\": \"Senior Dev\" }";
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .put("/api/positions/" + positionId)
            .then()
                .statusCode(200);
    }

    @Test(description = "[POS] Delete position - success", priority = 24)
    public void testDeletePosition() {
        System.out.println("--- [POS] Delete Position ---");
        if (positionId == null) { System.out.println("SKIP - ID null"); return; }
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .delete("/api/positions/" + positionId)
            .then()
                .statusCode(200);
        System.out.println("Position deleted");
    }

    // =============================================
    // 4. EMPLOYEE
    // =============================================

    @Test(description = "[EMP] Create employee - success", priority = 30)
    public void testCreateEmployee() {
        System.out.println("--- [EMP] Create Employee ---");
        String body = "{\n" +
            "  \"name\": \"John Doe\",\n" +
            "  \"email\": \"johndoe@ptdika.com\",\n" +
            "  \"phone\": \"081234567890\",\n" +
            "  \"address\": \"Jakarta\"\n" +
            "}";
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .post("/api/employees")
            .then()
                .extract().response();
        System.out.println("Status: " + res.statusCode() + " | Response: " + res.asString());
        employeeId = res.path("data.id");
    }

    @Test(description = "[EMP] Get all employees - success", priority = 31)
    public void testGetAllEmployees() {
        System.out.println("--- [EMP] Get All Employees ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/employees")
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[EMP] Get employee by ID - success", priority = 32)
    public void testGetEmployeeById() {
        System.out.println("--- [EMP] Get Employee By ID ---");
        if (employeeId == null) { System.out.println("SKIP - ID null"); return; }
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/employees/" + employeeId)
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[EMP] Update employee - success", priority = 33)
    public void testUpdateEmployee() {
        System.out.println("--- [EMP] Update Employee ---");
        if (employeeId == null) { System.out.println("SKIP - ID null"); return; }
        String body = "{ \"name\": \"John Updated\", \"phone\": \"089876543210\" }";
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .put("/api/employees/" + employeeId)
            .then()
                .statusCode(200);
    }

    @Test(description = "[EMP] Delete employee - success", priority = 34)
    public void testDeleteEmployee() {
        System.out.println("--- [EMP] Delete Employee ---");
        if (employeeId == null) { System.out.println("SKIP - ID null"); return; }
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .delete("/api/employees/" + employeeId)
            .then()
                .statusCode(200);
        System.out.println("Employee deleted");
    }

    // =============================================
    // 5. PERMISSION MANAGEMENT
    // =============================================

    @Test(description = "[PERM] Create permission - success", priority = 40)
    public void testCreatePermission() {
        System.out.println("--- [PERM] Create Permission ---");
        String body = "{ \"name\": \"read:employee\", \"description\": \"Can read employee\" }";
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .post("/api/permissions")
            .then()
                .extract().response();
        System.out.println("Status: " + res.statusCode() + " | Response: " + res.asString());
        permissionId = res.path("data.id");
    }

    @Test(description = "[PERM] Get all permissions - success", priority = 41)
    public void testGetAllPermissions() {
        System.out.println("--- [PERM] Get All Permissions ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/permissions")
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[PERM] Get permission by ID - success", priority = 42)
    public void testGetPermissionById() {
        System.out.println("--- [PERM] Get Permission By ID ---");
        if (permissionId == null) { System.out.println("SKIP - ID null"); return; }
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/permissions/" + permissionId)
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[PERM] Update permission - success", priority = 43)
    public void testUpdatePermission() {
        System.out.println("--- [PERM] Update Permission ---");
        if (permissionId == null) { System.out.println("SKIP - ID null"); return; }
        String body = "{ \"name\": \"read:employee:updated\", \"description\": \"Updated\" }";
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .put("/api/permissions/" + permissionId)
            .then()
                .statusCode(200);
    }

    @Test(description = "[PERM] Delete permission - success", priority = 44)
    public void testDeletePermission() {
        System.out.println("--- [PERM] Delete Permission ---");
        if (permissionId == null) { System.out.println("SKIP - ID null"); return; }
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .delete("/api/permissions/" + permissionId)
            .then()
                .statusCode(200);
        System.out.println("Permission deleted");
    }

    // =============================================
    // 6. ROLE MANAGEMENT
    // =============================================

    @Test(description = "[ROLE] Create role - success", priority = 50)
    public void testCreateRole() {
        System.out.println("--- [ROLE] Create Role ---");
        String body = "{ \"name\": \"Staff\", \"description\": \"Regular staff\", \"permissionIds\": [] }";
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .post("/api/roles")
            .then()
                .extract().response();
        System.out.println("Status: " + res.statusCode() + " | Response: " + res.asString());
        roleId = res.path("data.id");
    }

    @Test(description = "[ROLE] Get all roles - success", priority = 51)
    public void testGetAllRoles() {
        System.out.println("--- [ROLE] Get All Roles ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/roles")
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[ROLE] Get role by ID - success", priority = 52)
    public void testGetRoleById() {
        System.out.println("--- [ROLE] Get Role By ID ---");
        if (roleId == null) { System.out.println("SKIP - ID null"); return; }
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/roles/" + roleId)
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[ROLE] Update role - success", priority = 53)
    public void testUpdateRole() {
        System.out.println("--- [ROLE] Update Role ---");
        if (roleId == null) { System.out.println("SKIP - ID null"); return; }
        String body = "{ \"name\": \"Senior Staff\", \"description\": \"Senior\", \"permissionIds\": [] }";
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .put("/api/roles/" + roleId)
            .then()
                .statusCode(200);
    }

    @Test(description = "[ROLE] Delete role - success", priority = 54)
    public void testDeleteRole() {
        System.out.println("--- [ROLE] Delete Role ---");
        if (roleId == null) { System.out.println("SKIP - ID null"); return; }
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .delete("/api/roles/" + roleId)
            .then()
                .statusCode(200);
        System.out.println("Role deleted");
    }

    // =============================================
    // 7. USER MANAGEMENT
    // =============================================

    @Test(description = "[USER] Get all users - success", priority = 60)
    public void testGetAllUsers() {
        System.out.println("--- [USER] Get All Users ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/users")
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[USER] Create user - success", priority = 61)
    public void testCreateUser() {
        System.out.println("--- [USER] Create User ---");
        String body = "{\n" +
            "  \"email\": \"newuser@ptdika.com\",\n" +
            "  \"password\": \"p4ssw0rd\",\n" +
            "  \"name\": \"New User\"\n" +
            "}";
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .post("/api/users")
            .then()
                .extract().response();
        System.out.println("Status: " + res.statusCode() + " | Response: " + res.asString());
        userId = res.path("data.id");
    }

    @Test(description = "[USER] Get user by ID - success", priority = 62)
    public void testGetUserById() {
        System.out.println("--- [USER] Get User By ID ---");
        if (userId == null) { System.out.println("SKIP - ID null"); return; }
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .get("/api/users/" + userId)
            .then()
                .statusCode(200)
                .extract().response();
        Assert.assertTrue((boolean) res.path("success"));
    }

    @Test(description = "[USER] Update user - success", priority = 63)
    public void testUpdateUser() {
        System.out.println("--- [USER] Update User ---");
        if (userId == null) { System.out.println("SKIP - ID null"); return; }
        String body = "{ \"name\": \"Updated User\" }";
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
            .when()
                .put("/api/users/" + userId)
            .then()
                .statusCode(200);
    }

    @Test(description = "[USER] Delete user - success", priority = 64)
    public void testDeleteUser() {
        System.out.println("--- [USER] Delete User ---");
        if (userId == null) { System.out.println("SKIP - ID null"); return; }
        RestAssured
            .given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .delete("/api/users/" + userId)
            .then()
                .statusCode(200);
        System.out.println("User deleted");
    }

    @Test(description = "[USER] User role access - compare with admin (negative)", priority = 65)
    public void testUserRoleAccess() {
        System.out.println("--- [USER] User Role vs Admin Access ---");
        Response res = RestAssured
            .given()
                .header("Authorization", "Bearer " + userToken)
            .when()
                .get("/api/users")
            .then()
                .extract().response();
        System.out.println("User accessing /api/users: " + res.statusCode());
        System.out.println("Response: " + res.asString());
        // User mungkin 403 Forbidden karena bukan admin
    }

}
