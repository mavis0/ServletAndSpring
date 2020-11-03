package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import service.UserService;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index() {
        return userService.hello();
    }
}
