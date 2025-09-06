package madres.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper: ObjectMapper = ObjectMapper().apply {
    // Register the Kotlin module for better Kotlin support
    registerKotlinModule()
    registerModule(JavaTimeModule())

    // Set the naming strategy for properties to SNAKE_CASE
    // PropertyNamingStrategy.SNAKE_CASE is deprecated, use PropertyNamingStrategies.SNAKE_CASE
    setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

    // Serialization Features
    enable(SerializationFeature.INDENT_OUTPUT) // Pretty-print JSON for readability
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Write dates as ISO-8601 strings, not timestamps
    // enable(SerializationFeature.FAIL_ON_EMPTY_BEANS) // Enabled by default, throws error if bean has no properties

    // Deserialization Features
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // Ignore unknown JSON properties during deserialization
    enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY) // Handle single values as arrays for collections
    enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT) // Treat empty arrays as null for objects
}