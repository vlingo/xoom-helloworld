// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.infra.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.hello.infra.GreetingData;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * The actor that is responsible for running queries.
 */
public class QueriesActor extends StateStoreQueryActor implements Queries {
  public QueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<GreetingData> greetingOf(String greetingId) {
    return queryStateFor(greetingId, GreetingData.class, GreetingData.empty());
  }

  @Override
  public Completes<Collection<GreetingData>> greetings() {
    return streamAllOf(GreetingData.class, new ArrayList<>());
  }
}
