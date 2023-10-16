package ru.tinkoff.petstore.client.order.model.configuration

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus.toFicusConfig
import net.ceedubs.ficus.readers.ValueReader

case class OrderClientConfiguration(baseUrl: String)

object OrderClientConfiguration {
  def load(config: Config): OrderClientConfiguration =
    config.as[OrderClientConfiguration]("pet-store.order")

  private implicit val petClientConfigurationReader: ValueReader[OrderClientConfiguration] =
    ValueReader.relative(config => OrderClientConfiguration(config.getString("base-url")))
}
