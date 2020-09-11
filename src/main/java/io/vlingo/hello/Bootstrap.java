// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello;

import io.vlingo.actors.Stage;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.annotation.initializer.Xoom;

/**
 * Start the service with a Server.
 */
@Xoom(name = "hello-world")
@ResourceHandlers(packages = "io.vlingo.hello.resource")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Stage stage) {
  }

}
