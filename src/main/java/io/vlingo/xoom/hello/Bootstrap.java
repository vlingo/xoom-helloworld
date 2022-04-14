// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello;

import io.vlingo.xoom.hello.infra.persistence.CommandModelStoreProvider;
import io.vlingo.xoom.hello.infra.persistence.ProjectionDispatcherProvider;
import io.vlingo.xoom.hello.infra.persistence.QueryModelStoreProvider;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

/**
 * Start the service with a Server.
 */
@Xoom(name = "hello-world")
@ResourceHandlers(packages = "io.vlingo.xoom.hello.infra.resource")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
    final StatefulTypeRegistry registry = new StatefulTypeRegistry(grid.world());
    QueryModelStoreProvider.using(grid.world().stage(), registry);
    CommandModelStoreProvider.using(grid.world().stage(), registry, ProjectionDispatcherProvider.using(grid.world().stage()).storeDispatcher);
  }
}
