plugins {
    // This connects to your build-logic convention
    id("buildlogic.kafka-conventions")
    `java-library`
}

avro {
    // Note the "is" prefix for Boolean properties in Kotlin DSL
    isCreateSetters.set(true)
    isEnableDecimalLogicalType.set(true)

    // String properties don't usually have the "is" prefix
    fieldVisibility.set("PRIVATE")
    outputCharacterEncoding.set("UTF-8")
}