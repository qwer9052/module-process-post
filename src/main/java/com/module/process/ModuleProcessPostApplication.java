package com.module.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJpaAuditing
@EntityScan(basePackages = {"com.module.db"})
@EnableJpaRepositories(basePackages = {"com.module.domain.*.entityrepo"})
public class ModuleProcessPostApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleProcessPostApplication.class, args);
    }

}
