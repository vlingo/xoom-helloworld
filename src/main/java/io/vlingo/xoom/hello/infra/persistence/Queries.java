// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.infra.persistence;

import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.hello.infra.GreetingData;

/**
 * The queries that may be run against the query model.
 */
public interface Queries {
  Completes<GreetingData> greetingOf(String greetingId);
  Completes<Collection<GreetingData>> greetings();
}
