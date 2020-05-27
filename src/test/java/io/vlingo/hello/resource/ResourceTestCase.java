package io.vlingo.hello.resource;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vlingo.hello.XoomInitializer;
import io.vlingo.hello.infra.persistence.CommandModelStoreProvider;
import io.vlingo.hello.infra.persistence.ProjectionDispatcherProvider;
import io.vlingo.hello.infra.persistence.QueryModelStoreProvider;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

abstract class ResourceTestCase {
  private static final AtomicInteger portNumberPool = new AtomicInteger(18080);
  private int portNumber;
  private XoomInitializer xoom;

  @Before
  public void setUp() {
    portNumber = portNumberPool.getAndIncrement();
    xoom = new XoomInitializer(new String[]{"" + portNumber});
    Boolean startUpSuccess = xoom.server.startUp().await(100);
    assertThat(startUpSuccess, is(equalTo(true)));
  }

  @After
  public void cleanUp() {
    xoom.server.stop();

    QueryModelStoreProvider.reset();
    ProjectionDispatcherProvider.reset();
    CommandModelStoreProvider.reset();
  }

  protected RequestSpecification givenJsonClient() {
    return given()
        .port(portNumber)
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON);
  }
}
