package com.module.process.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * 서비스의 기본 설정값 초기화
 */
@Configuration
@ComponentScan(
        basePackages = {
                "com.module.core",
                "com.module.domain",
        }
)
public class BasicConfiguration {

//        @Bean
//        public JwtAspect jwtAspect(){
//                return new JwtAspect();
//        }



}
