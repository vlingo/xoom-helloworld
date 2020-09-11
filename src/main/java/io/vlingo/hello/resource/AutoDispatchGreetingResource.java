// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello.resource;

import io.vlingo.common.Completes;
import io.vlingo.hello.infra.GreetingData;
import io.vlingo.hello.infra.MessageData;
import io.vlingo.hello.infra.persistence.QueriesActor;
import io.vlingo.hello.model.Greeting;
import io.vlingo.hello.model.GreetingEntity;
import io.vlingo.http.Method;
import io.vlingo.http.Response;
import io.vlingo.xoom.annotation.autodispatch.*;

@AutoDispatch(path = "/greetings")
@Model(protocol = Greeting.class, actor = GreetingEntity.class, data = GreetingData.class)
@Queries(protocol = io.vlingo.hello.infra.persistence.Queries.class, actor = QueriesActor.class)
public interface AutoDispatchGreetingResource {

    @Route(method = Method.POST, handler = "defineWith(stage, data.message, data.description)")
    @ResponseAdapter("GreetingData.from")
    Completes<Response> defineGreeting(@Body GreetingData data);

    @Route(method = Method.PATCH, path = "/{greetingId}/message", handler = "withMessage(message.value)")
    @ResponseAdapter("GreetingData.from")
    Completes<Response> changeGreetingMessage(@Id String greetingId, @Body MessageData message);

    @Route(method = Method.GET, path = "/{greetingId}", handler = "greetingOf(greetingId)")
    Completes<Response> queryGreetings(@Id String greetingId);

}

