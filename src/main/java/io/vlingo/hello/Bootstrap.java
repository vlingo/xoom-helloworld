// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello;

import io.vlingo.actors.Stage;
import io.vlingo.hello.infra.persistence.CommandModelStoreProvider;
import io.vlingo.hello.infra.persistence.ProjectionDispatcherProvider;
import io.vlingo.hello.infra.persistence.QueryModelStoreProvider;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.Xoom;

/**
 * Start the service with a Server.
 */
@Xoom(name = "hello-world")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(Stage stage) {
    final StatefulTypeRegistry registry = new StatefulTypeRegistry(stage.world());
    QueryModelStoreProvider.using(stage, registry);
    CommandModelStoreProvider.using(stage, registry, ProjectionDispatcherProvider.using(stage).storeDispatcher);
  }

}
