// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.infra.resource;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.Created;
import static io.vlingo.xoom.http.Response.Status.NotFound;
import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.ResponseHeader.ContentType;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.ResponseHeader.headers;
import static io.vlingo.xoom.http.ResponseHeader.of;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;
import static io.vlingo.xoom.http.resource.ResourceBuilder.patch;
import static io.vlingo.xoom.http.resource.ResourceBuilder.post;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.hello.infra.DescriptionData;
import io.vlingo.xoom.hello.infra.GreetingData;
import io.vlingo.xoom.hello.infra.MessageData;
import io.vlingo.xoom.hello.infra.persistence.Queries;
import io.vlingo.xoom.hello.infra.persistence.QueryModelStoreProvider;
import io.vlingo.xoom.hello.model.Greeting;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;

public class GreetingResource extends DynamicResourceHandler {

  private final Queries queries;

  public GreetingResource(final Stage stage) {
    super(stage);
    this.queries = QueryModelStoreProvider.instance().queries;
  }

  public Completes<Response> defineGreeting(GreetingData data) {
    return Greeting
      .defineWith(stage(), data.message, data.description)
      .andThenTo(state -> Completes.withSuccess(Response.of(Created, headers(of(Location, greetingLocation(state.id))).and(of(ContentType, "application/json")), serialized(GreetingData.from(state)))));
  }

  public Completes<Response> changeGreetingMessage(String greetingId, MessageData message) {
    return resolve(greetingId)
            .andThenTo(greeting -> greeting.withMessage(message.value))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(GreetingData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound));
  }

  public Completes<Response> changeGreetingDescription(String greetingId, DescriptionData description) {
    return resolve(greetingId)
            .andThenTo(greeting -> greeting.withDescription(description.value))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(GreetingData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound));
  }

  public Completes<Response> queryGreetings() {
    return queries.greetings()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))));
  }

  public Completes<Response> queryGreeting(String greetingId) {
    return queries.greetingOf(greetingId)
            .andThenTo(GreetingData.empty(), data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
            .otherwise(noData -> Response.of(NotFound));
  }

  @Override
  public Resource<?> routes() {
    return resource("Greeting Resource",
      post("/greetings")
        .body(GreetingData.class)
        .handle(this::defineGreeting),
      patch("/greetings/{greetingId}/message")
        .param(String.class)
        .body(MessageData.class)
        .handle(this::changeGreetingMessage),
      patch("/greetings/{greetingId}/description")
        .param(String.class)
        .body(DescriptionData.class)
        .handle(this::changeGreetingDescription),
      get("/greetings")
        .handle(this::queryGreetings),
      get("/greetings/{greetingId}")
        .param(String.class)
        .handle(this::queryGreeting));
  }
  
  private String greetingLocation(final String greetingId) {
    return "/greetings/" + greetingId;
  }

  private Completes<Greeting> resolve(final String greetingId) {
    return stage().actorOf(Greeting.class, stage().addressFactory().from(greetingId));
  }
}
