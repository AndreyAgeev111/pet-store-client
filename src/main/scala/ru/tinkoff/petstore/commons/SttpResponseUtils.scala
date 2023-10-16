package ru.tinkoff.petstore.commons

import cats.ApplicativeError
import io.circe.Decoder
import sttp.client3.circe.asJsonAlways
import sttp.client3.ResponseAs

object SttpResponseUtils {
  def unwrapResponse[F[_], T: Decoder](implicit
      F: ApplicativeError[F, Throwable],
  ): ResponseAs[F[T], Any] =
    asJsonAlways[T].map(F.fromEither)
}
