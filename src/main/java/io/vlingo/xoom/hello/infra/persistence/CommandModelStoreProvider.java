// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.hello.infra.persistence;

import java.util.Arrays;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.hello.model.GreetingState;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class CommandModelStoreProvider {
  private static CommandModelStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;

  public static CommandModelStoreProvider instance() {
    return instance;
  }

  @SuppressWarnings("rawtypes")
  public static CommandModelStoreProvider using(Stage stage, StatefulTypeRegistry registry, Dispatcher dispatcher) {
    if (instance != null) return instance;
    
    StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(GreetingState.class, new GreetingStateAdapter());
    new EntryAdapterProvider(stage.world()); // future

    Protocols storeProtocols =
            stage.actorFor(
                    new Class<?>[] { StateStore.class, DispatcherControl.class },
                    Definition.has(InMemoryStateStoreActor.class, Definition.parameters(Arrays.asList(dispatcher))));

    Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

    instance = new CommandModelStoreProvider(registry, storeWithControl._1, storeWithControl._2);

    return instance;
  }

  public static void reset() {
    instance = null;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CommandModelStoreProvider(StatefulTypeRegistry registry, StateStore store, DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;

    registry.register(new Info(store, GreetingState.class, GreetingState.class.getSimpleName()));
  }
}
