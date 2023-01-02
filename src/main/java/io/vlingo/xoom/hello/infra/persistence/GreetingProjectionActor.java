// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.infra.persistence;

import io.vlingo.xoom.hello.infra.GreetingData;
import io.vlingo.xoom.hello.model.Greeting.Operation;
import io.vlingo.xoom.hello.model.GreetingState;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;

public class GreetingProjectionActor extends StateStoreProjectionActor<GreetingData> {
  private Operation becauseOf;

  public GreetingProjectionActor() {
    super(QueryModelStoreProvider.instance().store);
  }

  @Override
  protected GreetingData currentDataFor(Projectable projectable) {
    becauseOf = Operation.valueOf(projectable.becauseOf()[0]);

    final GreetingState state = projectable.object();
    final GreetingData current = GreetingData.from(state);

    return current;
  }

  @Override
  protected GreetingData merge(GreetingData previousData, int previousVersion, GreetingData currentData, int currentVersion) {
    GreetingData merged;
    
    switch (becauseOf) {
    case GreetingDefined:
      merged = currentData;
      break;
    case GreetingMessageChange:
      merged = GreetingData.from(previousData.id, currentData.message, currentData.messageChangedCount, previousData.description, previousData.descriptionChangedCount);
      break;
    case GreetingDescriptionChanged:
      merged = GreetingData.from(previousData.id, previousData.message, previousData.messageChangedCount, currentData.description, currentData.descriptionChangedCount);
      break;
    default:
      merged = currentData;
    }

    return merged;
  }
}
