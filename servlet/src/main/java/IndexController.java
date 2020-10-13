
public class IndexController {
    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index.html", "user", "liaojing");
    }

    @GetMapping("/news")
    public ModelAndView news(){
        return new ModelAndView("index.html", "user", "news");
    }
}
