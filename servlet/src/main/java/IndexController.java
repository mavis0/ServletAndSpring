
public class IndexController {
    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index.html", "user", "liaojing");
    }

    @GetMapping("/news")
    public ModelAndView news(){
        return new ModelAndView("index.html", "user", "news");
    }

    @GetMapping("/slow/hello")
    public ModelAndView slowHello(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        return new ModelAndView("index.html", "user", "showHello");
    }
}
