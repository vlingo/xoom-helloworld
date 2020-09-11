// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.hello.infra;

import io.vlingo.hello.infra.persistence.*;
import io.vlingo.hello.model.Greeting;
import io.vlingo.hello.model.GreetingState;
import io.vlingo.xoom.annotation.persistence.*;

import static io.vlingo.xoom.annotation.persistence.Persistence.StorageType.STATE_STORE;

@Persistence(basePackage = "io.vlingo.hello", storageType = STATE_STORE, cqrs = true)
@Projections({
        @Projection(actor = GreetingProjectionActor.class, becauseOf = {Greeting.Operation.class})
})
@EnableQueries({
        @QueriesEntry(protocol = Queries.class, actor = QueriesActor.class)
})
@Adapters({GreetingState.class, GreetingData.class})
public class PersistenceSetup {

}
