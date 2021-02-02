package io.vlingo.hello.infra.resource;

import io.vlingo.hello.infra.DescriptionData;
import io.vlingo.hello.infra.GreetingData;
import io.vlingo.hello.infra.MessageData;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class GreetingResourceTest extends ResourceTestCase {

  @Test
  public void itCreatesNewGreeting() {
    givenJsonClient()
        .body(greetingData("Message", "Description"))
        .when()
        .post("/greetings")
        .then()
        .statusCode(201)
        .header("Location", matchesRegex("/greetings/[0-9]+"))
        .body(
            "id", notNullValue(),
            "message", equalTo("Message"),
            "description", equalTo("Description"),
            "messageChangedCount", equalTo(0),
            "descriptionChangedCount", equalTo(0)
        );
  }

  @Test
  public void itGetsAnExistingGreeting() {
    String location = givenGreetingWasCreated(greetingData("Message", "Description"));

    givenJsonClient()
        .when()
        .get(location)
        .then()
        .statusCode(200)
        .body(
            "id", equalTo(locationToId(location)),
            "message", equalTo("Message"),
            "description", equalTo("Description"),
            "messageChangedCount", equalTo(0),
            "descriptionChangedCount", equalTo(0)
        );
  }

  @Test
  public void itReturns404IfGreetingIsNotFound() {
    givenJsonClient()
        .when()
        .get("/greetings/42424242")
        .then()
        .statusCode(404);
  }

  @Test
  public void itGetsExistingGreetings() {
    givenGreetingWasCreated(greetingData("Message", "Description"));

    givenJsonClient()
        .when()
        .get("/greetings")
        .then()
        .statusCode(200)
        .body(
            "[0].id", notNullValue(),
            "[0].message", equalTo("Message"),
            "[0].description", equalTo("Description"),
            "[0].messageChangedCount", equalTo(0),
            "[0].descriptionChangedCount", equalTo(0)
        );
  }

  @Test
  public void itChangesTheMessage() {
    String id = locationToId(
        givenGreetingWasCreated(greetingData("Message", "Description"))
    );

    givenJsonClient()
        .body(messageData("New Message"))
        .when()
        .patch(String.format("/greetings/%s/message", id))
        .then()
        .statusCode(200)
        .body(
            "id", equalTo(id),
            "message", equalTo("New Message"),
            "description", equalTo("Description"),
            "messageChangedCount", equalTo(1),
            "descriptionChangedCount", equalTo(0)
        );
  }

  @Test
  public void itReturns404IfGreetingCannotBeFoundForChangingTheMessage() {
    givenJsonClient()
        .body(messageData("New Message"))
        .when()
        .patch("/greetings/42424242/message")
        .then()
        .statusCode(404);
  }

  @Test
  public void itChangesTheDescription() {
    String id = locationToId(
        givenGreetingWasCreated(greetingData("Message", "Description"))
    );

    givenJsonClient()
        .body(descriptionData("New Description"))
        .when()
        .patch(String.format("/greetings/%s/description", id))
        .then()
        .statusCode(200)
        .body(
            "id", equalTo(id),
            "message", equalTo("Message"),
            "description", equalTo("New Description"),
            "messageChangedCount", equalTo(0),
            "descriptionChangedCount", equalTo(1)
        );
  }

  @Test
  public void itReturns404IfGreetingCannotBeFoundForChangingTheDescription() {
    givenJsonClient()
        .body(descriptionData("New Description"))
        .when()
        .patch("/greetings/42424242/description")
        .then()
        .statusCode(404);
  }

  private String givenGreetingWasCreated(GreetingData greetingData) {
    return givenJsonClient()
        .body(greetingData)
        .when()
        .post("/greetings")
        .then()
        .statusCode(201)
        .header("Location", matchesRegex("/greetings/[0-9]+"))
        .extract()
        .header("Location");
  }

  private String locationToId(String location) {
    return location.replaceFirst("/greetings/", "");
  }

  private GreetingData greetingData(String message, String description) {
    return new GreetingData("", message, description);
  }

  private MessageData messageData(String newMessage) {
    return new MessageData(newMessage);
  }

  private DescriptionData descriptionData(String newDescription) {
    return new DescriptionData(newDescription);
  }
}
