// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.infra;

import io.vlingo.xoom.hello.model.GreetingState;

/**
 * Greeting data used for HTTP request body, response body,
 * as well as resource queries.
 */
public class GreetingData {
  private static GreetingData Empty = new GreetingData("", "", 0, "", 0);
  
  public final String id;
  public final String message;
  public final int messageChangedCount;
  public final String description;
  public final int descriptionChangedCount;

  public static GreetingData empty() {
    return Empty;
  }

  public static GreetingData from(GreetingState state) {
    return new GreetingData(state.id, state.message.value, state.message.changeCount, state.description.value, state.description.changeCount);
  }

  public static GreetingData from(String id, String message, int messageChangedCount, String description, int descriptionChangedCount) {
    return new GreetingData(id, message, messageChangedCount, description, descriptionChangedCount);
  }

  public GreetingData(String id, String message, int messageChangedCount, String description, int descriptionChangedCount) {
    this.id = id;
    this.message = message;
    this.messageChangedCount = messageChangedCount;
    this.description = description;
    this.descriptionChangedCount = descriptionChangedCount;
  }

  public GreetingData(String id, String message, String description) {
    this(id, message, 0, description, 0);
  }
}
