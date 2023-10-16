package ru.tinkoff.petstore.client.order

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.typesafe.config.{Config, ConfigFactory}
import org.asynchttpclient.DefaultAsyncHttpClient
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers
import org.testcontainers.containers.wait.strategy.Wait
import ru.tinkoff.petstore.client.order.model.configuration.OrderClientConfiguration
import ru.tinkoff.petstore.client.order.model.request.CreateOrderRequest
import ru.tinkoff.petstore.client.order.model.response.OrderResponse
import ru.tinkoff.petstore.client.utils.TestData.{date, id, petId}
import sttp.client3.SttpBackend
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

import java.io.File

class OrderClientImpl2Spec
    extends AsyncFreeSpec
    with AsyncIOSpec
    with Matchers
    with OrderClientImplIntSpecUtils
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
      withContainers { _ =>
        client
          .createOrder(CreateOrderRequest(petId))
          .asserting(_ shouldBe OrderResponse(id = id, petId = petId, date = date))
      }
    }
  }

  "findOrder" - {
    "find order by id" in {
      withContainers { _ =>
        client
          .findOrder(id)
          .asserting(_ shouldBe Some(OrderResponse(id = id, petId = petId, date = date)))
      }
    }
  }
}

trait OrderClientImplIntSpecUtils {
  val config: Config = ConfigFactory.load()
  val orderConfig: OrderClientConfiguration = OrderClientConfiguration.load(config)
  val backend: SttpBackend[IO, Any] =
    AsyncHttpClientCatsBackend.usingClient[IO](new DefaultAsyncHttpClient())
  val client: OrderClientImpl[IO] = new OrderClientImpl[IO](backend, orderConfig)
}
