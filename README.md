# Spring Boot Experience
![Maven Central Version v1](https://img.shields.io/maven-central/v/org.vxwo.springboot.experience/spring-boot-experience?filter=1.*&label=SpringBoot2.x)
![Maven Central Version v3](https://img.shields.io/maven-central/v/org.vxwo.springboot.experience/spring-boot-experience?filter=3.*&label=SpringBoot3.x)

Experience accumulation for Spring Boot 3.  
Organize various experience tools accumulated using SpringBoot in various projects.

# Modules

| **Module**                                                                 | **Description** |
|----------------------------------------------------------------------------|-----------------|
| [spring-boot-experience-web](spring-boot-experience-web/README.md)         | Web support     |
| [spring-boot-experience-redis](spring-boot-experience-redis/README.md)     | Redis support   |
| [spring-boot-experience-mybatis](spring-boot-experience-mybatis/README.md) | MyBatis support |

# Usage

```xml

<project>

  ... others

  <properties>
    <sbexp.version>x.x.x</sbexp.version>
    ... others
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.vxwo.springboot.experience</groupId>
      <artifactId>spring-boot-experience-web</artifactId>
      <version>${sbexp.version}</version>
    </dependency>

    <dependency>
      <groupId>org.vxwo.springboot.experience</groupId>
      <artifactId>spring-boot-experience-redis</artifactId>
      <version>${sbexp.version}</version>
    </dependency>

    <dependency>
      <groupId>org.vxwo.springboot.experience</groupId>
      <artifactId>spring-boot-experience-mybatis</artifactId>
      <version>${sbexp.version}</version>
    </dependency>

    ... others
  </dependencies>

  ... others

</project>

```
