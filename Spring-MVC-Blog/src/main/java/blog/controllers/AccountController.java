package blog.controllers;

import blog.forms.LoginForm;
import blog.forms.RegisterForm;
import blog.models.User;
import blog.services.NotificationService;
import blog.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/users/login")
    public String showLoginForm(LoginForm loginForm,HttpServletRequest request){
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData != null)
            return "redirect:/404";

        return "users/login";
    }

    @RequestMapping(value="/users/login",method= RequestMethod.POST)
    public String showLoginForm(@Valid LoginForm loginForm, BindingResult bindingResult,HttpServletRequest request){

             if (bindingResult.hasErrors()){
                   notificationService.addErrorMessage("Please enter correct data in the fields!");
                return "users/login";
             }

            User user = userService.findByUsernameAndPassword(loginForm.getUsername(),loginForm.getPassword());

            if (user == null){
                notificationService.addErrorMessage("Wrong username or password!");
                return "users/login";
            }
            request.getSession().setAttribute("LOGGEDIN_USER", loginForm);
            request.getSession().setAttribute("LOGGEDIN_USER_NAME", user.getFullName());
            request.getSession().setAttribute("LOGGEDIN_USER_USERNAME", user.getUsername());
            request.getSession().setAttribute("LOGGEDIN_USER_ID", user.getId());

            notificationService.addInfoMessage("Welcome! " + user.getUsername());
            return "redirect:/";
        }

    @RequestMapping("/users/register")
    public String showRegisterForm(RegisterForm registerForm,HttpServletRequest request){

        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData != null)
            return "redirect:/404";

        return "users/register";
    }

    @RequestMapping(value="/users/register",method= RequestMethod.POST)
    public String showRegisterForm(@Valid RegisterForm registerForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            notificationService.addErrorMessage("Please enter correct data in the fields!");
            return "users/register";
        }

        User user = new User(registerForm.getUsername(),registerForm.getFullname(),registerForm.getPassword());
        userService.create(user);

        notificationService.addInfoMessage("Registered successfully! You may now login!");
        return "redirect:/users/login";
    }
}
