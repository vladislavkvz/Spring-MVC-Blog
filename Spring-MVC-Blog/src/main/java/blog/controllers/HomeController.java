package blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public  String home(){
        return "index";
    }


    @RequestMapping("/404")
    public  String pageNotFound(){
        return "404";
    }
}
