import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener  implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sec) {
        System.out.println("WebApp " + sec.getServletContext() + " initialized.");
    }

    public void contextDestoryed(ServletContextEvent sec) {
        System.out.println("WebApp " + sec.getServletContext() +  " destroyed.");
    }
}
