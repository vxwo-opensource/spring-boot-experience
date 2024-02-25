spring-boot-experience-redis
==============================================

Redis support

# Configuration

- [Default Settings](src/main/resources/experience/experience-redis.yml)
- [Example Settings](src/test/resources/application.yml)

prefix: sbexp.redis

| *Key*             | *Type* | *Required* | *Default* | *Description*                    |
|-------------------|--------|------------|-----------|----------------------------------|
| namespace         | String |            |           | Global **Key Prefix** for redis  |
| namespace-stuffix | String |            | _:_       | Force stuffix for **Key Prefix** |

# Processors

## org.vxwo.springboot.experience.redis.processor.RedisLockProcessor

### Methods

- LockDurationSession lock(String lockKey, Duration duration)

  Enter lock session and own it

- LockDurationSession lock(String lockKey, Duration duration, Duration waitting)

  Enter lock session and own it, supports waitting

- boolean boolean unlock(LockDurationSession session)

  Leave lock session

## org.vxwo.springboot.experience.redis.processor.RedisFrequencyProcessor

### Methods

- FrequencyDurationSession enterFrequencyDuration(String, Duration)

  Enter frequency session and own it

- boolean leaveFrequencyDuration(FrequencyDurationSession)

  Leave frequency session
