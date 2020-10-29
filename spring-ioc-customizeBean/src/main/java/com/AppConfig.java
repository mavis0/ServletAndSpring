package com;

import com.validator.Validators;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Validators validators = context.getBean(Validators.class);
        validators.validate("email1", "password1", "name1");
        ZoneId zoneId = context.getBean(ZoneId.class);
        System.out.println(zoneId.getRules());
    }

    @Bean
    ZoneId createZoneId() {
        return ZoneId.of("Z");
    }
}
