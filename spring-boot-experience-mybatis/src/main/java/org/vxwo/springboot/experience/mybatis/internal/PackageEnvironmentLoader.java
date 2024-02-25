package org.vxwo.springboot.experience.mybatis.internal;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

/**
 * @author vxwo-team
 */

public class PackageEnvironmentLoader implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
            SpringApplication application) {
        try {
            PropertySource<?> propertySource = loader.load("experience-mybatis",
                    new ClassPathResource("experience/experience-mybatis.yml")).get(0);
            environment.getPropertySources().addLast(propertySource);
        } catch (IOException ex) {
        }
    }

}
