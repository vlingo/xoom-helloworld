// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.hello.infra.DescriptionData;
import io.vlingo.hello.infra.GreetingData;
import io.vlingo.hello.infra.MessageData;
import io.vlingo.hello.infra.persistence.Queries;
import io.vlingo.hello.infra.persistence.QueryModelStoreProvider;
import io.vlingo.hello.model.Greeting;
import io.vlingo.hello.model.GreetingEntity;
import io.vlingo.http.Response;
import io.vlingo.http.resource.DynamicResourceHandler;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

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
            .otherwise(noGreeting -> Response.of(NotFound, greetingLocation(greetingId)));
  }

  public Completes<Response> changeGreetingDescription(String greetingId, DescriptionData description) {
    return resolve(greetingId)
            .andThenTo(greeting -> greeting.withDescription(description.value))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(GreetingData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, greetingLocation(greetingId)));
  }

  public Completes<Response> queryGreetings() {
    return queries.greetings()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))));
  }

  public Completes<Response> queryGreeting(String greetingId) {
    return queries.greetingOf(greetingId)
            .andThenTo(GreetingData.empty(), data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
            .otherwise(noData -> Response.of(NotFound, greetingLocation(greetingId)));
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
    return stage().actorOf(Greeting.class, stage().addressFactory().from(greetingId), GreetingEntity.class);
  }
}
