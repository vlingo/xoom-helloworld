// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello.infra.persistence;

import java.util.Arrays;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.hello.infra.GreetingData;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

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
    return completes().with(Arrays.asList()); // TODO: implement
  }
}
