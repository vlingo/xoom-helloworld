// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.hello;

import io.vlingo.actors.World;
import io.vlingo.hello.resource.HelloResource;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

/**
 * Start the service with a Server.
 */
public class Bootstrap {
  private static Bootstrap instance;
  private static int Port = 18080;

  public final Server server;
  public final World world;

  public static final Bootstrap instance() {
    if (instance == null) {
      instance = new Bootstrap(Port);
    }

    return instance;
  }

  public static void main(String[] args) {
    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = Port;
      System.out.println("hello-world: Command line does not provide a valid port; defaulting to: " + port);
    }

    instance = new Bootstrap(port);
  }

  private Bootstrap(int port) {
    this.world = World.startWithDefaults("hello-world");

    final HelloResource helloResource = new HelloResource();

    this.server = Server.startWith(world.stage(), Resources.are(helloResource.routes()), port, Sizing.define(), Timing.define());

    registerShutdownHook();

    world.defaultLogger().info("============================================");
    world.defaultLogger().info("Started hello-world service on port " + port );
    world.defaultLogger().info("============================================");
  }

  private void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        if (instance != null) {
          server.stop();

          world.defaultLogger().info("\n");
          world.defaultLogger().info("========================");
          world.defaultLogger().info("Stopping hello-world... ");
          world.defaultLogger().info("========================");
          
          world.terminate();
        }
      }
    });
  }
}
