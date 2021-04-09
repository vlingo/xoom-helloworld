package io.vlingo.xoom.hello.infra.resource;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class HelloResourceTest extends ResourceTestCase {

  @Test
  public void itGreetsTheWorldIfNoGameWasGiven() {
    givenJsonClient()
        .when()
        .get("/hello")
        .then()
        .statusCode(200)
        .body(is(equalTo("Hello, World!")));
  }

  @Test
  public void itGreetsUsingTheGivenName() {
    givenJsonClient()
        .when()
        .get("/hello/Kuba")
        .then()
        .statusCode(200)
        .body(is(equalTo("Hello, Kuba!")));
  }
}
