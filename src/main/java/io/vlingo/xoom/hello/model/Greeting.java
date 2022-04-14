// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.model;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

/**
 * The Greeting protocol.
 */
public interface Greeting {
  static Completes<GreetingState> defineWith(Stage stage, String message, String description) {
    Address address = stage.addressFactory().uniquePrefixedWith("g-");
    Greeting greeting = stage.actorFor(Greeting.class, Definition.has(GreetingEntity.class, Definition.parameters(address.idString())), address);
    return greeting.defineWith(message, description);
  }

  /**
   * Defines my initial message and description. I may not
   * be redefined using this message.
   * @param message the String initial message of the Greeting
   * @param description the String initial description of the Greeting
   * @return {@code Completes<GreetingState>}
   */
  Completes<GreetingState> defineWith(String message, String description);

  /**
   * Changes my message.
   * @param message the new String message of the Greeting
   * @return {@code Completes<GreetingState>}
   */
  Completes<GreetingState> withMessage(String message);

  /**
   * Changes my description.
   * @param description the new String description of the Greeting
   * @return {@code Completes<GreetingState>}
   */
  Completes<GreetingState> withDescription(String description);

  /**
   * The operations that may be performed on a Greeting. 
   */
  static enum Operation {
    GreetingDefined, GreetingMessageChange, GreetingDescriptionChanged
  }
}
