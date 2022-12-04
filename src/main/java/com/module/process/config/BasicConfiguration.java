package com.module.process.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * 서비스의 기본 설정값 초기화
 */
@Configuration
@ComponentScan(
        basePackages = {
                "com.module.core",
                "com.module.domain",
                "com.module.db.config",
                "com.module.cache",
        }
)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJpaAuditing
@EntityScan(basePackages = {"com.module.db"})
@EnableJpaRepositories(basePackages = {"com.module.domain.*.entityrepo"})
public class BasicConfiguration {

//        @Bean
//        public JwtAspect jwtAspect(){
//                return new JwtAspect();
//        }



}
