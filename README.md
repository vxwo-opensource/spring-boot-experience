# spring-boot-experience
Experience accumulation for Spring Boot.  
Organize various experience tools accumulated using SpringBoot in various projects.

# Modules

| **Module**                                                             | **Description** |
|------------------------------------------------------------------------|-----------------|
| [spring-boot-experience-web](spring-boot-experience-web/README.md)     | Web support     |
| [spring-boot-experience-redis](spring-boot-experience-redis/README.md) | Redis support   |

# Usage

```xml

<project>

  ... others

  <properties>
    <sbexp.version>1.1.0</sbexp.version>
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

    ... others
  </dependencies>

  ... others

</project>

```
