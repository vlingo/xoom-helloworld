// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello.infra.resource;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.DynamicResourceHandler;
import io.vlingo.http.resource.Resource;

public class HelloResource extends DynamicResourceHandler {
  private static final String Hello = "Hello, #!";
  private static final String World = "World";

  public HelloResource(final Stage stage) { super(stage); }

  public Completes<Response> hello() {
    return helloWhom(World);
  }

  public Completes<Response> helloWhom(String whom) {
    return Completes.withSuccess(Response.of(Ok, Hello.replace("#", whom)));
  }

  public Resource<?> routes() {
    return resource("Hello Resource",
      get("/hello")
        .handle(this::hello),
      get("/hello/{whom}")
        .param(String.class)
        .handle(this::helloWhom));
  }

}
