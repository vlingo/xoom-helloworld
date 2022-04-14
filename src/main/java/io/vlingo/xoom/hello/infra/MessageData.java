// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.infra;

/**
 * Greeting data used for HTTP request body, response body,
 * as well as resource queries.
 */
public class MessageData {
  public final String value;

  public static MessageData empty() {
    return new MessageData("");
  }

  public static MessageData from(String message) {
    return new MessageData(message);
  }

  public MessageData(String message) {
    this.value = message;
  }
}
