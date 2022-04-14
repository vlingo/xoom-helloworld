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
public class DescriptionData {
  public final String value;

  public static DescriptionData empty() {
    return new DescriptionData("");
  }

  public static DescriptionData from(String description) {
    return new DescriptionData(description);
  }

  public DescriptionData(String description) {
    this.value = description;
  }
}
