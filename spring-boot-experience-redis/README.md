spring-boot-experience-redis
==============================================

# Configuration

- [Default Settings](src/main/resources/experience/experience-redis.yml)
- [Example Settings](src/test/resources/application.yml)

prefix: sbexp.redis

| *Key*             | *Type* | *Required* | *Default* | *Description*                    |
|-------------------|--------|------------|-----------|----------------------------------|
| namespace         | String |            |           | Global **Key Prefix** for redis  |
| namespace-stuffix | String |            | _:_       | Force stuffix for **Key Prefix** |

# Processors

## org.vxwo.springboot.experience.redis.processor.RedisFrequencyProcessor

### Methods

- FrequencyDurationSession enterFrequencyDuration(String, Duration)

  Enter frequency session and own it

- boolean leaveFrequencyDuration(FrequencyDurationSession)

  Leave frequency session
