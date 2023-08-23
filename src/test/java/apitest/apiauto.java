package apitest;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class apiauto {

    @Test
    public void testGetUserList() {

        RestAssured.baseURI = "https://reqres.in/";
        given().when().get("api/users?page=2")
                .then()
                .log().all()
                .assertThat().statusCode(200);

    }

}
