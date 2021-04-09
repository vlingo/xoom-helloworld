package io.vlingo.xoom.hello.infra.resource;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vlingo.xoom.hello.XoomInitializer;
import io.vlingo.xoom.hello.infra.persistence.CommandModelStoreProvider;
import io.vlingo.xoom.hello.infra.persistence.ProjectionDispatcherProvider;
import io.vlingo.xoom.hello.infra.persistence.QueryModelStoreProvider;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

abstract class ResourceTestCase {
  private XoomInitializer xoom;

  @Before
  public void setUp() throws Exception {
    XoomInitializer.main(new String[]{});
    xoom = XoomInitializer.instance();
    Boolean startUpSuccess = xoom.server().startUp().await(100);
    assertThat(startUpSuccess, is(equalTo(true)));
  }

  @After
  public void cleanUp() {
    xoom.server().stop();

    QueryModelStoreProvider.reset();
    ProjectionDispatcherProvider.reset();
    CommandModelStoreProvider.reset();
  }

  protected RequestSpecification givenJsonClient() {
    return given()
        .port(18080)
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON);
  }
}
