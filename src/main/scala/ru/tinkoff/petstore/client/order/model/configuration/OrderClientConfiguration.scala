package ru.tinkoff.petstore.client.order.model.configuration

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus.toFicusConfig
import net.ceedubs.ficus.readers.ValueReader

import scala.concurrent.duration.{FiniteDuration, SECONDS}

case class OrderClientConfiguration(baseUrl: String, timeout: FiniteDuration)

object OrderClientConfiguration {
  def load(config: Config): OrderClientConfiguration =
    config.as[OrderClientConfiguration]("pet-store.order")

  private implicit val petClientConfigurationReader: ValueReader[OrderClientConfiguration] =
    ValueReader.relative(config =>
      OrderClientConfiguration(
        baseUrl = config.getString("base-url"),
        timeout = FiniteDuration.apply(config.getLong("timeout"), SECONDS),
      ),
    )
}
