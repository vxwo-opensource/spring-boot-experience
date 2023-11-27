spring-boot-experience-redis
==============================================

# Configuration

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
