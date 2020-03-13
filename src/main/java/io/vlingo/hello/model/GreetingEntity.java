// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public class GreetingEntity extends StatefulEntity<GreetingState> implements Greeting {
  private GreetingState state;

  public GreetingEntity(String id) {
    super(id);
  }

  @Override
  public Completes<GreetingState> defineWith(String message, String description) {
    if (state == null) {
      return apply(GreetingState.has(id, message, description), Operation.GreetingDefined.name(), () -> state);
    } else {
      return completes().with(state);
    }
  }

  @Override
  public Completes<GreetingState> withMessage(String message) {
    return apply(state.withMessage(message), Operation.GreetingMessageChange.name(), () -> state);
  }

  @Override
  public Completes<GreetingState> withDescription(String description) {
    return apply(state.withDescription(description), Operation.GreetingDescriptionChanged.name(), () -> state);
  }

  @Override
  protected void state(GreetingState state) {
    this.state = state;
  }

  @Override
  protected Class<GreetingState> stateType() {
    return GreetingState.class;
  }
}
