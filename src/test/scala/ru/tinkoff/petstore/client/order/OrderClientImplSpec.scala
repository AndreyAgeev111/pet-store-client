package ru.tinkoff.petstore.client.order

import cats.effect._
import cats.effect.testing.scalatest.AsyncIOSpec
import com.softwaremill.macwire.wire
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.petstore.client.order.model.request.CreateOrderRequest
import ru.tinkoff.petstore.client.order.model.response.OrderResponse
import ru.tinkoff.petstore.utils.TestData.{date, id, petId}
import ru.tinkoff.petstore.wirings.DefaultWirings

class OrderClientImplSpec
    extends AsyncFreeSpec
    with AsyncIOSpec
    with Matchers
    with OrderClientImplSpecUtils {
  "createOrder" - {
    "create order by id" in {
      client
        .createOrder(CreateOrderRequest(petId))
        .asserting(_ shouldBe OrderResponse(id = id, petId = petId, date = date))
    }
  }
  "findOrder" - {
    "find order by id" in {
      client
        .findOrder(id)
        .asserting(_ shouldBe Some(OrderResponse(id = id, petId = petId, date = date)))
    }
  }
}

trait OrderClientImplSpecUtils extends DefaultWirings {
  val client: OrderClientImpl[IO] = wire[OrderClientImpl[IO]]
}
