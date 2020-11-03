package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/config.properties")
public class UserService {

    @Value("A.b")
    private String s;

    public String hello() {
        return s;
    }
}
