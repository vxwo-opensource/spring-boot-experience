package org.vxwo.springboot.experience.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vxwo.springboot.experience.mybatis.GeneralSqlHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
@Configuration
public class MybatisAutoConfiguration {

    @Autowired
    public MybatisAutoConfiguration(MybatisConfig value, SqlSessionFactory sqlSessionFactory) {
        GeneralSqlHelper.initialize(value, sqlSessionFactory.getConfiguration());

        if (log.isInfoEnabled()) {
            log.info("MyBatis actived");
        }
    }

    @Bean
    public GeneralSqlHelper getMybatsSessionHelper() {
        return new GeneralSqlHelper();
    }
}
