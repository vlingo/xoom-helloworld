// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.model;

/**
 * State held by the Greeting.
 */
public final class GreetingState {
  public final String id;
  public final Message message;
  public final Description description;

  public static GreetingState has(String id, String message, String description) {
    return new GreetingState(id, Message.with(message), Description.with(description));
  }

  public GreetingState withMessage(String message) {
    return new GreetingState(id, this.message.changeWith(message), description);
  }

  public GreetingState withDescription(String description) {
    return new GreetingState(id, message, this.description.changeWith(description));
  }

  private GreetingState(String id, Message message, Description description) {
    this.id = id;
    this.message = message;
    this.description = description;
  }


  /**
   * Message of the GreetingState, which tracks change counts.
   */
  public static final class Message {
    public final String value;
    public final int changeCount;

    public static Message with(String message) {
      return new Message(message, 0);
    }

    public Message changeWith(String message) {
      return new Message(message, changeCount + 1);
    }

    private Message(String message, int changeCount) {
      this.value = message;
      this.changeCount = changeCount;
    }
  }

  /**
   * Description of the GreetingState, which tracks change counts.
   */
  public static final class Description {
    public final String value;
    public final int changeCount;

    public static Description with(String description) {
      return new Description(description, 0);
    }

    public Description changeWith(String description) {
      return new Description(description, changeCount + 1);
    }

    private Description(String description, int changeCount) {
      this.value = description;
      this.changeCount = changeCount;
    }
  }
}
