package ru.tinkoff.petstore.client.order.model.request

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

import java.util.UUID

final case class CreateOrderRequest(petId: UUID)

object CreateOrderRequest {
  implicit val createOrderRequestEncoder: Encoder[CreateOrderRequest] =
    deriveEncoder[CreateOrderRequest]
}
