# vlingo-helloworld

A "Hello, World!" service that demonstrates the use of the standard components of the vlingo/PLATFORM.

Start a console/command window so you can build the `vlingo-helloworld` and start it by executing the built `jar`:

```
$ mvn clean package
...
java -jar target/vlingo-helloworld-withdeps.jar
```

The above `java` command executes the `jar` on the default port `18080`. If you would like to use
a different port, you must provide it on the command line. This command uses port `8080`.

```
java -jar target/vlingo-helloworld-withdeps.jar 8080
```

You may `curl` with `GET` methods on the following resources.

```
curl -i -X GET http://localhost:8080/hello
```

The above `curl` responds with `200 OK` and the content body entity `"Hello, World!"`

You may also provide a path parameter to indicate to whom the service should say `"Hello"`.
The second example responds with `200 OK` and the content body entity `"Hello, Me!"`

```
curl -i -X GET http://localhost:8080/hello/Me
```
