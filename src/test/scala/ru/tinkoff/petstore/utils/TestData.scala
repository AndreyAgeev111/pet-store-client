package ru.tinkoff.petstore.utils

import java.time.Instant
import java.util.UUID

object TestData {
  val petId: UUID = UUID.fromString("93e9c52a-6a88-11ee-8c99-0242ac120002")
  val id: UUID = UUID.fromString("a7580748-6a88-11ee-8c99-0242ac120002")
  val date: Instant = Instant.ofEpochMilli(1697475306664L)
  val name: String = "name"
  val description: String = "description"
  val orderResponseString: String =
    s"""
       |{
       | "id": "$id",
       | "petId": "$petId",
       | "date": "$date"
       |}
       |""".stripMargin
}
