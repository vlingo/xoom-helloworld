# vlingo-helloworld

A "Hello, World!" service that demonstrates the use of the standard components of the vlingo/PLATFORM.

Open a console/command window so you can build the `vlingo-helloworld` and start it by executing the built `jar`:

```
$ mvn clean package
...
$ java -jar target/vlingo-helloworld-withdeps.jar
```

The above `java` command executes the `jar` on the default port `18080`. If you would like to use
a different port, you must provide it on the command line. This command uses port `8080`.

```
$ java -jar target/vlingo-helloworld-withdeps.jar 8080
```

The following examples assume that have started the service with the default port, `18080`.

There are two resources, each with multiple endpoints. These are discussed next.

## Hello Resource

The first resource is used to get `"Hello, World!"` and similar messages.

You may `curl` with `GET` methods on the following resources.

```
$ curl -i -X GET http://localhost:18080/hello
```

The above `curl` responds with `200 OK` and the content body entity `"Hello, World!"`

You may also provide a path parameter to indicate to whom the service should say `"Hello"`.
The second example responds with `200 OK` and the content body entity `"Hello, Me!"`

```
$ curl -i -X GET http://localhost:18080/hello/Me
```

In this Hello resource example there is only one component involved. See the source code:

```
io.vlingo.hello.resource.HelloResource
```

## Greeting Resource

The second resource is a bit more involved, and is used to maintain any number of `Greeting` messages. These greetings
have the following data associated with them.

- `id`: a unique identity
- `message`: a text message
- `messageChangedCount`: the number of times the message text has changed since first being defined
- `description`: a text description of the message
- `descriptionChangedCount`: the number of times the description text has changed since first being defined

The first operation used is to define a new `Greeting`. To do so you `POST` a `JSON` object to the `/greetings` URI.

```
$ curl -i -X POST -H "Content-Type: application/json" -d '{"id":"","message":"Hey","messageChangedCount":"0","description":"Says Hey","descriptionChangedCount":"0" }' http://localhost:18080/greetings
``` 

Following this you may query the new `Greeting`. See the `Location` header provided by the `POST` response for the URI of the resource to `GET`.
For example:

```
HTTP/1.1 201 Created
Location: /greetings/242
Content-Length: 105

{"id":"242","message":"Hey","messageChangedCount":0,"description":"Says Hey","descriptionChangedCount":0}
```

The location of the new `Greeting` resource is `/greetings/242`. Let's `GET` that resource.

```
$ curl -i -X GET http://localhost:18080/greetings/242
```

You should see the following:

```
HTTP/1.1 200 OK
Content-Length: 105

{"id":"242","message":"Hey","messageChangedCount":0,"description":"Says Hey","descriptionChangedCount":0}
```

Next `PATCH` the `Greeting` resource's `message`.

```
$ curl -i -X PATCH -H "Content-Type: application/json" -d '{"value":"Yo"}' http://localhost:18080/greetings/242/message
```

The resource responds with the following. Note that the `message` has changed to `"Yo"`, and the `messageChangedCount` is now `1`.
Also notice that the `description` and the `descriptionChangedCount` remain unchanged.

```
HTTP/1.1 200 OK
Content-Length: 104

{"id":"242","message":"Yo","messageChangedCount":1,"description":"Says Hey","descriptionChangedCount":0}
```

Next `PATCH` the `Greeting` resource's `description`.


```
$ curl -i -X PATCH -H "Content-Type: application/json" -d '{"value":"Says Yo"}' http://localhost:18080/greetings/242/description
```

The resource responds with the following. Note that the `description` has changed to `"Says Yo"`, and the `descriptionChangedCount` is now `1`.

```
HTTP/1.1 200 OK
Content-Length: 103

{"id":"242","message":"Yo","messageChangedCount":1,"description":"Says Yo","descriptionChangedCount":1}
```

Now both the `message` and the `description` and the corresponding counts have all changed.

In this `Greeting` resource example there are various component sets involved. See the following source code.

```
io.vlingo.hello.Bootstrap

io.vlingo.hello.resource.GreetingResource

io.vlingo.hello.model.Greeting
io.vlingo.hello.model.GreetingEntity
io.vlingo.hello.model.GreetingState

io.vlingo.hello.infra.persistence.GreetingProjectionActor

io.vlingo.hello.infra.persistence.Queries
io.vlingo.hello.infra.persistence.QueriesActor

io.vlingo.hello.infra.persistence.* - lower-level persistence setup and storage access
```


Documentation for the above components is found in the following links.

**Suggestion: Right click and open in a new tab.**

[Starting the World](https://docs.vlingo.io/vlingo-actors#starting-and-terminating-the-actor-runtime)

[CQRS Command and Query Models](https://docs.vlingo.io/vlingo-lattice/entity-cqrs)

[StatefulEntity](https://docs.vlingo.io/vlingo-lattice/entity-cqrs#statefulentity-example)

[StateStore Persistence](https://docs.vlingo.io/vlingo-symbio/state-storage)

[Query Model Projections](https://docs.vlingo.io/vlingo-lattice/projections)

[Reactive Storage](https://docs.vlingo.io/vlingo-symbio)
