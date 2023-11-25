spring-boot-experience-web
==============================================

# Filters

## Chain

CORS -> Request Logging -> Authorization -> Frequency Control -> Request Logging (Aspect)

## CORS

### Configuration

prefix: sbexp.web.cors

| *Key*         | *Type*        | *Required* | *Default* | *Description*                                              |
|---------------|---------------|------------|-----------|------------------------------------------------------------|
| enabled       | boolean       |            | false     | Switch                                                     |
| parse-referer | boolean       |            | false     | Parse *Origin* from *Referer* when it was lossed           |
| allow-origins | Array[String] | Y          |           | Allowed *Access-Control-Allow-Origin* values, empty is _*_ |

## Request logging

### Configuration

prefix: sbexp.web.logging

| *Key*                   | *Type*        | *Required* | *Default* | *Description*                          |
|-------------------------|---------------|------------|-----------|----------------------------------------|
| enabled                 | boolean       |            | false     | Switch                                 |
| ignore-request-headers  | boolean       |            | false     | Ignore request headers                 |
| ignore-response-headers | boolean       |            | true      | Ignore response headers                |
| respnse-body-limit-kb   | int           |            | 500       | KB size limit for ignore response body |
| stacktrace-limit-lines  | int           |            | 5         | Lines limit for Exception stack trace  |
| include-paths           | Array[String] |            | [/]       | Paths for logging                      |

### Implementation

#### org.vxwo.springboot.experience.web.handler.RequestLoggingHandler

Handle the logging

## ApiKey Authorization

### Configuration

prefix: sbexp.web.api-key

| *Key*        | *Type*               | *Required* | *Default*            | *Description*                                                 |
|--------------|----------------------|------------|----------------------|---------------------------------------------------------------|
| enabled      | boolean              |            | false                | Switch                                                        |
| header-keys  | Array[String]        |            | [X-Api-Key, Api-Key] | Which *Header* for get the *ApiKey* value                     |
| parse-bearer | boolean              |            | false                | Use *Bearer Token* as *ApiKey* when no found in *header-keys* |
| bearer-keys  | Array[String]        |            | [Bearer, Bear]       | Which *Key* for parse *Bearer Token* value                    |
| path-rules   | Array[OwnerPathRule] | Y          |                      | Path Rules                                                    |

#### OwnerPathRule

| *Key*  | *Type*       | *Required* | *Default* | *Description*         |
|--------|--------------|------------|-----------|-----------------------|
| path   | String       | Y          |           | Path prefix           |
| owners | Array[Owner] | Y          |           | Pair of key and owner |

##### Owner

| *Key* | *Type*  | *Required* | *Default* | *Description* |
|-------|---------|------------|-----------|---------------|
| key   | String  | Y          |           | The *key*     |
| owner | String] |            | unknow    | Onwer of key  |

### Implementation

#### org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler

Handle the authorization failure

## Bearer Authorization

### Configuration

prefix: sbexp.web.bearer

| *Key*       | *Type*               | *Required* | *Default*      | *Description*                              |
|-------------|----------------------|------------|----------------|--------------------------------------------|
| enabled     | boolean              |            | false          | Switch                                     |
| bearer-keys | Array[String]        |            | [Bearer, Bear] | Which *Key* for parse *Bearer Token* value |
| path-rules  | Array[GroupPathRule] | Y          |                | Path Rules                                 |

#### GroupPathRule

| *Key*     | *Type*        | *Required* | *Default* | *Description*                      |
|-----------|---------------|------------|-----------|------------------------------------|
| path      | String        | Y          |           | Path prefix                        |
| excludes  | Array[String] |            |           | Child paths for exclude processing |
| optionals | Array[String] |            |           | Child paths for processing or not  |


### Implementation

#### org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler

Handle the authorization failure

#### org.vxwo.springboot.experience.web.handler.BearerAuthorizationHandler

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
| duration-ms   | int           | Y          |           | Milliseconds for fixed duration  |
| include-paths | Array[String] | Y          |           | Paths for fixed interval control |

### Implementation

#### org.vxwo.springboot.experience.web.handler.FrequencyControlFailureHandler

Handle the frequency control failure

#### org.vxwo.springboot.experience.web.handler.FrequencyControlHandler

Handle the frequency control
