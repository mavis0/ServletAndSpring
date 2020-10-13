import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {
    private Map<String, GetDispatcher> getMappings = new HashMap<>();
    private Map<String, PostDispatcher> postMappings = new HashMap<>();
    private List<Class<?>> controllers = List.of(IndexController.class);
    private ViewEngine viewEngine;

    private static final Set<Class<?>> supportedGetParameterTypes = Set.of(int.class, long.class, boolean.class,
            String.class, HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);

    private static final Set<Class<?>> supportedPostParameterTypes = Set.of(HttpServletRequest.class,
            HttpServletResponse.class, HttpSession.class);
    public void init() {
        System.out.println("init...");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (Class<?> controllerClass : controllers) {
            try {
                Object controllerInstance = controllerClass.getConstructor().newInstance();
                for (Method method: controllerClass.getMethods()) {
                    if (method.getAnnotation(GetMapping.class) != null) {
                        if (method.getReturnType() != ModelAndView.class && method.getReturnType() != void.class) {
                            throw new UnsupportedOperationException("Unsupported return type");
                        }
                        for (Class<?> parameterClass: method.getParameterTypes()) {
                            if (!supportedGetParameterTypes.contains(parameterClass)) {
                                throw new UnsupportedOperationException("Unsupported parameter type");
                            }
                        }
                        String[] parameterNames = Arrays.stream(method.getParameters()).map(p -> p.getName()).toArray(String[]::new);
                        String path = method.getAnnotation(GetMapping.class).value();
                        System.out.println("Found GET: " + path + " => " + method);
                        this.getMappings.put(path, new GetDispatcher(controllerInstance, method, parameterNames, method.getParameterTypes()));
                    }
                }
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        this.viewEngine = new ViewEngine(getServletContext());
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        System.out.println("request for the url:" + path);
        GetDispatcher dispatcher = this.getMappings.get(path);
        System.out.println(this.getMappings);
        if (dispatcher == null) {
            resp.sendError(404);
            return;
        }
        ModelAndView mv = null;
        try {
            mv = dispatcher.invoke(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mv == null) {
            return;
        }
        if (mv.view.startsWith("redirect:")){
            resp.sendRedirect(mv.view.substring(9));
        }
        PrintWriter pw = resp.getWriter();
        System.out.println(mv.view);
        this.viewEngine.render(mv, pw);
        pw.flush();
    }
}

class ModelAndView {
    Map<String, Object> model;
    String view;

    public ModelAndView(String view, String name, Object value) {
        this.view = view;
        this.model = new HashMap<>();
        this.model.put(name, value);
    }
}

abstract class AbstractDispatcher {

    public abstract ModelAndView invoke(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ReflectiveOperationException;
}

class GetDispatcher extends AbstractDispatcher{
    Object instance;
    Method method;
    String[] parameterNames;
    Class<?>[] parameterClasses;

    public GetDispatcher(Object controllerClass, Method method, String[] parameterNames, Class<?>[] parameterTypes) {
        super();
        this.instance = controllerClass;
        this.method = method;
        this.parameterClasses = parameterTypes;
        this.parameterNames = parameterNames;
    }

    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletResponse.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else if (parameterClass == int.class) {
                arguments[i] = Integer.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == long.class) {
                arguments[i] = Long.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == boolean.class) {
                arguments[i] = Boolean.valueOf(getOrDefault(request, parameterName, "false"));
            } else if (parameterClass == String.class) {
                arguments[i] = getOrDefault(request, parameterName, "");
            } else {
                throw new RuntimeException("Missing handler for type: " + parameterClass);
            }
        }
        return (ModelAndView) this.method.invoke(this.instance, arguments);
    }

    private String getOrDefault(HttpServletRequest request, String parameterName, String defaultValue) {
        String s = request.getParameter(parameterName);
        return s == null ? defaultValue : s;
    }
}

class PostDispatcher {
    Object instance;
    Method method;
    Class<?>[] parameterClasses;
    ObjectMapper objectMapper;
}
