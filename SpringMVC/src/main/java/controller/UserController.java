package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public void index(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, world!</h1>");
        pw.flush();
    }

//    @GetMapping("/")
//    public ModelAndView index(HttpSession session) {
//        Map<String, Object> model = new HashMap<>();
//        Map<String, Object> user = new HashMap<>();
//        user.put("name", "Bob");
//        model.put("user", user);
//        return new ModelAndView("index.html", model);
//    }
}
