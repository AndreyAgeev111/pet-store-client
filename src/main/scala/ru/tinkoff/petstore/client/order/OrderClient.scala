package ru.tinkoff.petstore.client.order

import cats.effect.kernel.Async
import cats.implicits._
import ru.tinkoff.petstore.client.order.model.configuration.OrderClientConfiguration
import ru.tinkoff.petstore.client.order.model.request.CreateOrderRequest
import ru.tinkoff.petstore.client.order.model.response.OrderResponse
import ru.tinkoff.petstore.commons.SttpResponseUtils
import sttp.client3.circe._
import sttp.client3.{SttpBackend, UriContext, basicRequest}

import java.util.UUID

trait OrderClient[F[_]] {
  def createOrder(request: CreateOrderRequest): F[OrderResponse]
  def findOrder(orderId: UUID): F[Option[OrderResponse]]
}

class OrderClientImpl[F[_]: Async](
    sttpBackend: SttpBackend[F, Any],
    orderClientConfiguration: OrderClientConfiguration,
) extends OrderClient[F] {
  override def createOrder(request: CreateOrderRequest): F[OrderResponse] = {
    val createOrderUrl = uri"${orderClientConfiguration.baseUrl}/api/v1/order"

    basicRequest
      .post(createOrderUrl)
      .body(request)
      .response(SttpResponseUtils.unwrapResponse[F, OrderResponse])
      .send(sttpBackend)
      .flatMap(_.body)
  }

  override def findOrder(orderId: UUID): F[Option[OrderResponse]] = {
    val listPetsUrl = uri"${orderClientConfiguration.baseUrl}/api/v1/order/$orderId"

    basicRequest
      .get(listPetsUrl)
      .response(SttpResponseUtils.unwrapResponse[F, Option[OrderResponse]])
      .send(sttpBackend)
      .flatMap(_.body)
  }
}
