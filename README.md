<h3 align="center">Opsgenie Kotlin SDK</h3>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/bnjns/opsgenie-kotlin-sdk.svg)](https://github.com/bnjns/opsgenie-kotlin-sdk/issues)
[![License](https://img.shields.io/github/license/bnjns/opsgenie-kotlin-sdk)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.bnjns.opsgenie/kotlin-sdk)](https://github.com/bnjns/opsgenie-kotlin-sdk)

</div>

---

<p align="center"> 
    A Kotlin SDK for interacting with the Opsgenie REST API.
</p>

## 🧐 About

<!-- TODO -->

## 🏁 Getting Started

This SDK is not yet available on Maven Central.

<!--

### Gradle

```
implementation "com.bnjns.opsgenie:opsgenie-kotlin-sdk:$version"
```

### Maven

```xml
<dependency>
  <groupId>com.bnjns.opsgenie</groupId>
  <artifactId>opsgenie-kotlin-sdk</artifactId>
  <version>{version}</version>
</dependency>
```

-->

## 🎈 Usage

See [the full documentation](https://bnjns.github.io/opsgenie-kotlin-sdk/) for more details. 

### Creating the client

```kotlin
import com.bnjns.opsgenie.opsgenieClient

val client = opsgenieClient {
    // You can use the EU API, instead of the US API
    eu()
    
    // You can use API key authentication
    credentials {
        apiKey("...")
    }
    
    // Or basic authentication
    credentials {
        basic(username = "...", password = "...")
    }
}
```

### Calling an API

Simply call the relevant API method:

```kotlin
val accountInfo = client.account.getAccountInfo()
```

For APIs where you need to configure the request, simply use the included DSL:

```kotlin
val onCalls = client.oncall.getOnCalls {
    name("The schedule name")
    startingNow()
}
```

For APIs which return a paginated response, the items will be returned along with the paging info, and you can
automatically fetch the next page using the included helper method:

```kotlin
val paginatedAlerts = client.alerts.listAlerts()
val nextPage = paginatedAlerts.nextPage() ?: throw IllegalStateException("No more pages to fetch")
```

## ⛏️ Built Using

- [Ktor Client](https://ktor.io/)
- [Kotest](https://kotest.io/)
- [Jackson](https://github.com/FasterXML/jackson)
- [KotlinLogging](https://github.com/MicroUtils/kotlin-logging)
- [Dokka](https://github.com/Kotlin/dokka)

## ✍️ Authors

- [bnjns](https://github.com/bnjns)
