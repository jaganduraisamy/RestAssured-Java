import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestCase {

    String LocationToSearch = "GATEWAY WEST";

    @Test
    public void PositiveVerifications(){
        given().
                param("searchVal",LocationToSearch).
                param("returnGeom","Y").
                param("getAddrDetails","Y").
                param("pageNum","1").
                when().
                get("https://developers.onemap.sg/commonapi/search").
                then().
                assertThat().statusCode(200). // Verify response code
                and().contentType(ContentType.JSON).  // Verify response content type
                and().assertThat().
                body("pageNum",equalTo(1)). // verify page number
                and().assertThat().
                body("results.SEARCHVAL",contains(LocationToSearch)); // verify search string is same
    }

    @Test
    public void VerifyWithEmptySearchValue() {

        given().
                param("searchVal", "").
                param("returnGeom", "Y").
                param("getAddrDetails", "Y").
                param("pageNum", "1").
                when().
                get("https://developers.onemap.sg/commonapi/search").
                then().
                assertThat().statusCode(400); // Verify response code

    }

    private SearchResult ExtractFromResponse(Response response) {
        SearchResult res = new SearchResult();

        JsonPath js = new JsonPath(response.getBody().asString());
        res.ADDRESS =  js.get("results[0].ADDRESS");
        res.BLK_NO =  js.get("results[0].BLK_NO");
        res.BUILDING =  js.get("results[0].BUILDING");
        res.POSTAL =  js.get("results[0].POSTAL");
        res.ROAD_NAME =  js.get("results[0].ROAD_NAME");
        res.SEARCHVAL =  js.get("results[0].SEARCHVAL");

        return res;
    }

    @Test
    public void VerifyPostalRequestResponse() {


        // Get Postal request value
        Response res = given(). //headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                param("searchVal",LocationToSearch).
                param("returnGeom","Y").
                param("getAddrDetails","Y").
                param("pageNum","1").
                when().
                get("https://developers.onemap.sg/commonapi/search").
                then().contentType(ContentType.JSON).
                assertThat().statusCode(200).
                and().contentType(ContentType.JSON).  // Verify response content type
                and().assertThat().
                body("pageNum",equalTo(1)).
                extract().response();

        SearchResult result1 = ExtractFromResponse(res);

        // Get response for Postal request
        Response res2 = given().
                param("searchVal",result1.POSTAL).
                param("returnGeom","Y").
                param("getAddrDetails","Y").
                param("pageNum","1").
                when().
                get("https://developers.onemap.sg/commonapi/search").
                then().assertThat().statusCode(200).
                and().contentType(ContentType.JSON).  // Verify response content type
                and().assertThat().
                body("pageNum",equalTo(1)).
                extract().response();

        SearchResult result2 = ExtractFromResponse(res2);

        Assert.assertTrue(SearchResult.AreEqual(result1,result2)); // Compare both response details

    }

    // TODO : Add test to verify schema

}
