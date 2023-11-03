package ru.tinkoff.petstore.service.order

import cats.effect.kernel.Async
import retry.retryingOnSomeErrors
import ru.tinkoff.petstore.client.order.OrderClient
import ru.tinkoff.petstore.client.order.model.request.CreateOrderRequest
import ru.tinkoff.petstore.client.order.model.response.OrderResponse
import ru.tinkoff.petstore.commons.RetryUtils

import java.util.UUID

trait OrderService[F[_]] {
  def createOrder(request: CreateOrderRequest): F[OrderResponse]

  def findOrder(orderId: UUID): F[Option[OrderResponse]]
}

class OrderServiceImpl[F[_]: Async](
    orderClient: OrderClient[F],
    retryUtils: RetryUtils[F],
) extends OrderService[F] {
  override def createOrder(request: CreateOrderRequest): F[OrderResponse] =
    retryingOnSomeErrors[OrderResponse](
      isWorthRetrying = retryUtils.isTimeoutException,
      policy = retryUtils.policy,
      onError = retryUtils.onError,
    )(orderClient.createOrder(request))

  override def findOrder(orderId: UUID): F[Option[OrderResponse]] =
    retryingOnSomeErrors[Option[OrderResponse]](
      isWorthRetrying = retryUtils.isTimeoutException,
      policy = retryUtils.policy,
      onError = retryUtils.onError,
    )(orderClient.findOrder(orderId))
}
