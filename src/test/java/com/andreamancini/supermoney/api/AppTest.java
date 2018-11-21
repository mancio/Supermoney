package com.andreamancini.supermoney.api;

import com.jayway.restassured.RestAssured;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

	
	// before any other class to connect to the server
	@BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost"; 
        RestAssured.port = 8080;
    }

	
	// after all the tests, to reset
    @AfterClass
    public static void unconfigureRestAssured() {
        RestAssured.reset();
    }
    
    
    // test if the server is online
    @Test
    public void Test1() {
    	given().when().get("/api/accounts").then().statusCode(200);
    	
    	
    	
    	
    	
    }

    // check if Mario has 2000 euro in his account
    @Test
    public void Test2() {
        final int id = get("/api/accounts").then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getInt("find { it.money==2000 }.id");
        get("/api/accounts/" + id).then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Mario Rossi"))
                .body("user", equalTo("supermario"));
                
    }

    // has Mario the id = 0?
    @Test
    public void Test3() {
        get("/api/accounts/0").then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(0))
                .body("name", equalTo("Mario Rossi"))
                .body("user", equalTo("supermario"));
    }
    
    

    @Test
    public void Test4() {
        get("/api/accounts/999").then()
                .assertThat()
                .statusCode(404);
    }

    
    @Test
    public void Test5() {
        given().body("{\n" +
                "    \"name\": \"Sergio Di Rio\",\n" +
        		"	 \"user\": \"teddy\",\n" +
                "    \"money\": \"4000\"\n" +
                
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
    }
    
    
    // try to add an account with wrong money amount 
    @Test
    public void Test6() {
        given().body("{\n" +
                "    \"name\": \"Chris Fail\",\n" +
                "    \"money\": \"jhvjhvkv\"\n" +
                
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(400);
    }

    //try to update user and amount in one account
    @Test
    public void Test7() {
        given().body("{\n" +
                "    \"user\": \"jam\",\n" +
                "    \"money\": \"7000\"\n" +
                "}")
                .when()
                .put("api/accounts/0")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(0))
                .body("name", equalTo("Mario Rossi"))
                .body("user", equalTo("jam"))
                .body("money", equalTo(7000));
    }

    
    // delete the account id 0 of Mario
    @Test
    public void Test8() {
        delete("/api/accounts/0").then()
                .assertThat()
                .statusCode(204);
        get("/api/accounts/0").then()
                .assertThat()
                .statusCode(404);
    }

    // try to delete an account with a wrong id
    @Test
    public void Test9() {
        delete("/api/accounts/999").then()
                .assertThat()
                .statusCode(404);
    }
    
    // transfer 20 euro from account 1 to account 2
    @Test 
    public void Test91() {
    	given().when().post("/api/transfer/1/to/2/20").then().assertThat().statusCode(200);
    }
    
    // try to transfer a wrong amount from id 1 to id 2 
    @Test
    public void Test92() {
    	given().when().post("/api/transfer/1/to/2/14354363").then().assertThat().statusCode(400);
    }

    

}