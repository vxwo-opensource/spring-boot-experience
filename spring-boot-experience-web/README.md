spring-boot-experience-web
==============================================

# Configuration

- [Default Settings](src/main/resources/experience/experience-web.yml)
- [Example Settings](src/test/resources/application.yml)

# Processors

## org.vxwo.springboot.experience.web.processor.PathProcessor

### Methods

- String createAbsoluteURI(String)

  Generateion absolute URI from relative URI

- String getRelativeURI(HttpServletRequest)

  Get relative URI from request

# Filters

## Chain

CORS -> Request Logging -> Authorization -> Frequency Control -> Request Logging (Aspect)

## CORS

### Configuration

prefix: sbexp.web.cors

| *Key*         | *Type*        | *Required* | *Default* | *Description*                                       |
|---------------|---------------|------------|-----------|-----------------------------------------------------|
| enabled       | boolean       |            | false     | Switch                                              |
| parse-referer | boolean       |            | false     | Parse *Origin* from *Referer* when it was lossed    |
| allow-origins | Array[String] |            |           | *Access-Control-Allow-Origin* values, empty is AUTO |

## Request logging

### Configuration

prefix: sbexp.web.logging

| *Key*                   | *Type*        | *Required* | *Default* | *Description*                          |
|-------------------------|---------------|------------|-----------|----------------------------------------|
| enabled                 | boolean       |            | false     | Switch                                 |
| ignore-request-headers  | boolean       |            | false     | Ignore request headers                 |
| request-header-keys     | Array{String} |            | [*]       | Include request header keys            |
| ignore-response-headers | boolean       |            | true      | Ignore response headers                |
| response-header-keys    | Array{String} |            | [*]       | Include response header keys           |
| respnse-body-limit-kb   | int           |            | 500       | KB size limit for ignore response body |
| stacktrace-limit-lines  | int           |            | 5         | Lines limit for Exception stack trace  |
| include-paths           | Array[String] |            | [/]       | Paths for logging                      |

### Implementations

- org.vxwo.springboot.experience.web.handler.RequestLoggingHandler

  Handle the logging

## ApiKey Authorization

### Configuration

prefix: sbexp.web.authorization.api-key

| *Key*        | *Type*               | *Required* | *Default*            | *Description*                               |
|--------------|----------------------|------------|----------------------|---------------------------------------------|
| enabled      | boolean              |            | false                | Switch                                      |
| header-keys  | Array[String]        |            | [X-Api-Key, Api-Key] | *Header* for get the *ApiKey* value         |
| parse-bearer | boolean              |            | false                | *Bearer Token* as *ApiKey* when no *Header* |
| bearer-keys  | Array[String]        |            | [Bearer, Bear]       | *Key* for parse *Bearer Token* value        |
| path-rules   | Array[OwnerPathRule] | Y          |                      | Path Rules                                  |

#### OwnerPathRule

| *Key*  | *Type*          | *Required* | *Default* | *Description*         |
|--------|-----------------|------------|-----------|-----------------------|
| tag    | String          |            | default   | Tag                   |
| path   | String          | Y          |           | Path prefix           |
| owners | Array[KeyOwner] | Y          |           | Pair of key and owner |

##### KeyOwner

| *Key* | *Type*  | *Required* | *Default* | *Description* |
|-------|---------|------------|-----------|---------------|
| key   | String  | Y          |           | The *key*     |
| owner | String] |            | none      | Onwer of key  |

### Implementations

- org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler

  Handle the authorization failure

## Bearer Authorization

### Configuration

prefix: sbexp.web.authorization.bearer

| *Key*       | *Type*               | *Required* | *Default*      | *Description*                        |
|-------------|----------------------|------------|----------------|--------------------------------------|
| enabled     | boolean              |            | false          | Switch                               |
| bearer-keys | Array[String]        |            | [Bearer, Bear] | *Key* for parse *Bearer Token* value |
| path-rules  | Array[GroupPathRule] | Y          |                | Path Rules                           |

#### GroupPathRule

| *Key*     | *Type*        | *Required* | *Default* | *Description*                      |
|-----------|---------------|------------|-----------|------------------------------------|
| tag       | String        |            | default   | Tag                                |
| path      | String        | Y          |           | Path prefix                        |
| excludes  | Array[String] |            |           | Child paths for exclude processing |
| optionals | Array[String] |            |           | Child paths for processing or not  |


### Implementations

- org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler

  Handle the authorization failure

- org.vxwo.springboot.experience.web.handler.BearerAuthorizationHandler

  Handle the bearer authorization

## Frequency Control

### Configuration

prefix: sbexp.web.frequency-control

| *Key*           | *Type*               | *Required* | *Default* | *Description*           |
|-----------------|----------------------|------------|-----------|-------------------------|
| enabled         | boolean              |            | false     | Switch                  |
| concurrency     | Concurrency          |            |           | Concurrency control     |
| fixed-intervals | Array[FixedInterval] |            | []        | Fixed Intervals control |

#### Concurrency

| *Key*         | *Type*        | *Required* | *Default* | *Description*                 |
|---------------|---------------|------------|-----------|-------------------------------|
| duration-ms   | int           |            | 60000     | Miliseconds for safe duration |
| include-paths | Array[String] |            |           | Paths for concurrency control |

#### FixedInterval

| *Key*         | *Type*        | *Required* | *Default* | *Description*                    |
|---------------|---------------|------------|-----------|----------------------------------|
| tag           | String        |            | default   | Tag                              |
| duration-ms   | int           | Y          |           | Milliseconds for fixed duration  |
| include-paths | Array[String] | Y          |           | Paths for fixed interval control |

### Implementations

- org.vxwo.springboot.experience.web.handler.FrequencyControlFailureHandler

  Handle the frequency control failure

- org.vxwo.springboot.experience.web.handler.FrequencyControlHandler

  Handle the frequency control
