package blog.controllers;

import blog.forms.LoginForm;
import blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/users")
    public String index(Model model,HttpServletRequest request){
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";

        model.addAttribute("users",userService.findAll());
        return "users/index";
    }

    @RequestMapping(value="/users/logout")
    public String logout (HttpServletRequest request) {

        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";

        request.getSession().setAttribute("LOGGEDIN_USER", null);
        request.getSession().setAttribute("LOGGEDIN_USER_NAME",null);
        request.getSession().setAttribute("LOGGEDIN_USER_USERNAME", null);
        request.getSession().setAttribute("LOGGEDIN_USER_ID", null);
        return "redirect:/";
    }
}