package modifyServletResponse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebFilter("/slow/*")
public class CacheFilter implements Filter {

    private Map<String, byte[]> cache = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String url = req.getRequestURI();

        byte[] data = this.cache.get(url);
        resp.setHeader("X-Cache-Hit", data == null ? "No" : "Yes");
        if (data == null) {
            System.out.println("Cache未找到");
            CacheHttpServletResponse wrapper = new CacheHttpServletResponse(resp);
            filterChain.doFilter(req, wrapper);
            data = wrapper.getContent();
            cache.put(url, data);
        } else {
            System.out.println("Cache命中");
        }
        ServletOutputStream output = resp.getOutputStream();
        output.write(data);
        output.flush();
    }
}
