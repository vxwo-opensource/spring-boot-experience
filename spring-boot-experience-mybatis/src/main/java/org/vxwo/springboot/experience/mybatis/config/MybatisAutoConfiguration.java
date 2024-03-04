package org.vxwo.springboot.experience.mybatis.config;

import java.util.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.vxwo.springboot.experience.mybatis.GeneralSqlHelper;
import org.vxwo.springboot.experience.mybatis.GeneralTableRegistrar;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class MybatisAutoConfiguration {

    @Autowired
    public MybatisAutoConfiguration(MybatisConfig value, SqlSessionFactory sqlSessionFactory,
            ApplicationContext applicationContext) {
        Set<String> basePackages = new HashSet<>();
        applicationContext.getBeansWithAnnotation(ComponentScan.class).forEach((name, instance) -> {
            basePackages.add(instance.getClass().getPackage().getName());
            for (ComponentScan scan : AnnotatedElementUtils
                    .getMergedRepeatableAnnotations(instance.getClass(), ComponentScan.class)) {
                basePackages.addAll(Arrays.asList(scan.basePackages()));
            }
        });

        GeneralSqlHelper.initialize(value, sqlSessionFactory.getConfiguration());
        for (String basePackage : basePackages) {
            GeneralTableRegistrar.registerTablesInPackage(basePackage);
        }

        if (log.isInfoEnabled()) {
            log.info("MyBatis actived");
        }
    }

    @Bean
    public GeneralSqlHelper getMybatsSessionHelper() {
        return new GeneralSqlHelper();
    }
}
