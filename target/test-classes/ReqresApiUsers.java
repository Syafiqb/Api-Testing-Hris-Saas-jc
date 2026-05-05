package com.juaracoding;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import io.github.cdimascio.dotenv.Dotenv;

public class ReqresApiUsers {

    String apikey;
    Dotenv dotenv = Dotenv.load();

    @BeforeClass
    public void setUp() {
        System.out.println("set up");
        RestAssured.baseURI = "https://reqres.in/api";
        apikey = dotenv.get("X_API_KEY");
    }

    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][] {
            {"morpheus", "leader"},
            {"neo", "qa engineer"},
            {"trinity", "hacker"}
        };
    }


    @Test(description = "get list users")
    public void getListUsers() {
        System.out.println("get list users");
        Response response = RestAssured
            .given()
                .header("x-api-key", apikey)
                .queryParam("page", 2)
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .extract()
                .response();
        int page = response.path("page"); 
        String firstName = response.path("data[0].first_name"); 
        System.out.println("page: " + page);
        Assert.assertEquals(page, 2);
        System.out.println("first name: " + firstName);
        Assert.assertEquals(firstName, "Michael");
    }

    @Test(description = "get single user")
    public void getSingleUser() {
        System.out.println("get single user");
        Response response = RestAssured
            .given()
                .header("x-api-key", apikey)
            .when()
                .get("/users/2")
            .then()
                .statusCode(200)
                .extract()
                .response();
        String firstName = response.path("data.first_name"); 
        System.out.println("first name: " + firstName);
        Assert.assertEquals(firstName, "Janet");
    }

    @Test(description = "get single user not found")
    public void getSingleUserNotFound() {
        System.out.println("get single user not found");
        RestAssured
            .given()    
                .header("x-api-key", apikey)
            .when()
                .get("/users/23")
            .then()
                .statusCode(404); 
    }

    @Test(description = "create new user", dataProvider = "userData")
    public void createNewUser(String name, String job) {
        System.out.println("create new user");
        Response response = RestAssured
            .given()
                .header("x-api-key", apikey)
                .header("Content-Type", "application/json")
                .body(Map.of("name", name, "job", job))
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .extract()
                .response();
        String createdname = response.path("name"); 
        String createdjob = response.path("job");
        System.out.println("name: " + createdname);
        Assert.assertEquals(createdname, name);
        System.out.println("job: " + createdjob);
        Assert.assertEquals(createdjob, job);
    }

    @Test(description = "update user")
    public void updateUser() {
        System.out.println("update user");
        String Name = "morpheus";
        String Job = "zion resident";
        Response response = RestAssured
            .given()
                .header("x-api-key", apikey)
                .header("Content-Type", "application/json")
                .body(Map.of("name", Name, "job", Job))
            .when()
                .put("/users/2")
            .then()
                .statusCode(200)
                .extract()
                .response();
        String name = response.path("name"); 
        String job = response.path("job");
        System.out.println("name: " + name);    
        System.out.println("job: " + job);
    }

    @Test(description = "delete user")
    public void deleteUser() {  
        System.out.println("delete user");
        String userId = "2";
        RestAssured
            .given()
                .header("x-api-key", apikey)
            .when()
                .delete("/users/{id}", userId)
            .then()
                .statusCode(204);
    }
    

    


}
