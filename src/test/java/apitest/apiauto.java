package apitest;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class apiauto {

    @Test
    public void testGetUserList() {

        //Define base URL
//        RestAssured.baseURI = "https://reqres.in/";
        //Test get api
        given().when().get("https://reqres.in/api/users?page=2")
                .then()
                .log().all() //Print all entier log to console
                .assertThat().statusCode(200) //Verifiaksi status code
                .assertThat().body("page", Matchers.equalTo(2)) //Verifiaksi page
                .assertThat().body("data", Matchers.hasSize(6)); //Veridikasi total data in page

    }

    @Test
    public void testPostData() {
//        RestAssured.baseURI = "https://reqres.in/";
        String valuename = "Lutfi";
        String valuejob = "QA";
        JSONObject object = new JSONObject();
        object.put("name", valuename);
        object.put("job", valuejob);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(object.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(valuename))
                .assertThat().body("job", Matchers.equalTo(valuejob))
                .assertThat().body("$", Matchers.hasKey("id"))
                .assertThat().body("$", Matchers.hasKey("createdAt"));
    }

    @Test
    public void testPutData() {

        int id = 2;
        String newName = "ESA PRASS";

        String name = given().when().get("https://reqres.in/api/users/"+id).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("https://reqres.in/api/users/"+id).getBody().jsonPath().get("data.last_name");
        String email = given().when().get("https://reqres.in/api/users/"+id).getBody().jsonPath().get("data.email");
        String avatar = given().when().get("https://reqres.in/api/users/"+id).getBody().jsonPath().get("data.avatar");
        System.out.println("Nama Sebelum di ubah : " + name);

        HashMap<String, Object> bodymap = new HashMap<>();
        bodymap.put("id", id);
        bodymap.put("first_name", newName);
        bodymap.put("last_name", lname);
        bodymap.put("email", email);
        bodymap.put("avatar", avatar);
        JSONObject jsonObject = new JSONObject(bodymap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("https://reqres.in/api/users/"+id)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));

    }

    @Test
    public void testPatchData() {

        int id = 3;
        String newName = "LEP";
        String name = given().when().get("https://reqres.in/api/users/"+id).getBody().jsonPath().get("data.first_name");
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("first_name", newName);
        JSONObject object = new JSONObject(bodyMap);
        System.out.println("Nama sebelum di ubah : " + name);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(object.toString())
                .patch("https://reqres.in/api/users/"+id)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));

    }

    @Test
    public void testDeleteData() {

        int id = 2;

        given().log().all()
                .when().delete("https://reqres.in/api/users/"+id)
                .then()
                .log().all()
                .assertThat().statusCode(204);

    }

    @Test
   public void testValidationJSONSchema() {

        int id = 2;
        File jsonfile = new File("src/test/resources/jsonSchma.json");

        given().log().all()
                .when().get("https://reqres.in/api/users/"+id)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(jsonfile));

    }

}
