package ru.tinkoff.petstore.service.order

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import org.asynchttpclient.DefaultAsyncHttpClient
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers
import org.testcontainers.containers.wait.strategy.Wait
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jFactory
import ru.tinkoff.petstore.client.order.OrderClientImpl
import ru.tinkoff.petstore.client.order.model.request.CreateOrderRequest
import ru.tinkoff.petstore.client.order.model.response.OrderResponse
import ru.tinkoff.petstore.commons.RetryUtilsImpl
import ru.tinkoff.petstore.utils.TestData.{date, id, petId}
import ru.tinkoff.petstore.wirings.DefaultWirings
import sttp.client3.SttpBackend
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

import java.io.File

class OrderServiceImplSpec
    extends AsyncFreeSpec
    with AsyncIOSpec
    with Matchers
    with OrderServiceImplSpecUtils
    with TestContainerForAll {

  override val containerDef: ContainerDef = DockerComposeContainer.Def(
    new File("src/test/resources/order/docker-compose.yml"),
    tailChildContainers = true,
    exposedServices = Seq(
      ExposedService("wiremock", 8080, Wait.forListeningPort()),
    ),
  )

  "createOrder" - {
    "create order by id" in {
      service
        .createOrder(CreateOrderRequest(petId))
        .asserting(_ shouldBe OrderResponse(id = id, petId = petId, date = date))
    }
  }

  "findOrder" - {
    "find order by id" in {
      service
        .findOrder(id)
        .asserting(_ shouldBe Some(OrderResponse(id = id, petId = petId, date = date)))
    }
  }

}

trait OrderServiceImplSpecUtils extends DefaultWirings {
  val backend: SttpBackend[IO, Any] =
    AsyncHttpClientCatsBackend.usingClient[IO](new DefaultAsyncHttpClient())
  val client: OrderClientImpl[IO] = new OrderClientImpl[IO](backend, orderClientConfiguration)
  val logger: Logger[IO] = Slf4jFactory.create[IO].getLogger
  val retryUtils: RetryUtilsImpl[IO] = new RetryUtilsImpl[IO](logger, retryConfiguration)
  val service: OrderServiceImpl[IO] = new OrderServiceImpl[IO](client, retryUtils)
}
