package blog.controllers;

import blog.forms.CreatePostForm;
import blog.forms.LoginForm;
import blog.models.Post;
import blog.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    @Autowired
    PostServiceImpl  postService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    NotificationServiceImpl notificationService;

    @RequestMapping("/posts")
    public String index(Model model,HttpServletRequest request){
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";

        model.addAttribute("posts",postService.findAll());
        return "posts/index";
    }

    @RequestMapping("/posts/myPosts")
    public String myPosts(Model model,HttpServletRequest request){
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";
        long loggedUserId = Long.valueOf(request.getSession().getAttribute("LOGGEDIN_USER_ID").toString());

        List<Post> posts = postService.findAll().stream().filter(post -> post.getAuthor().getId().equals(loggedUserId)).collect(Collectors.toList());

        model.addAttribute("posts",posts);
        return "posts/myPosts";
    }

    @RequestMapping("/posts/create")
    public String createPost(CreatePostForm createPostForm,HttpServletRequest request) {
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";

        return "posts/create";
    }

    @RequestMapping(value = "/posts/create",method = RequestMethod.POST )
    public String createPost(@Valid CreatePostForm createPostForm, BindingResult bindingResult,HttpServletRequest request,Model model){

        if (bindingResult.hasErrors()){
            notificationService.addErrorMessage("Please enter correct data in the fields!");
            return "posts/create";
        }

        long loggedUserId = Long.valueOf(request.getSession().getAttribute("LOGGEDIN_USER_ID").toString());

        Post post = new Post(createPostForm.getTitle(),createPostForm.getBody(),userService.findById(loggedUserId));
        postService.create(post);
        notificationService.addInfoMessage("Post created successfully!");

        return this.index(model,request);
    }

    @RequestMapping("/posts/view/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model,HttpServletRequest request){
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";

        model.addAttribute("post", postService.findById(id));
        return "posts/viewPost";
    }

    @RequestMapping("/posts/edit/{id}")
    public String editPost(@PathVariable("id") Long id,Post post,HttpServletRequest request,Model model){
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";

        post = postService.findById(id);

        long loggedUserId = Long.valueOf(request.getSession().getAttribute("LOGGEDIN_USER_ID").toString());
        if (!post.getAuthor().getId().equals(loggedUserId))
            return "redirect:/404";

        model.addAttribute("post",post);
        return "posts/updatePost";
    }

    @RequestMapping(value = "/posts/edit/{id}",method = RequestMethod.POST )
    public String createPost(@Valid Post post,@PathVariable("id") Long id,BindingResult bindingResult,HttpServletRequest request,Model model){

        if (bindingResult.hasErrors()){
            notificationService.addErrorMessage("Please enter correct data in the fields!");
            return "posts/create";
        }

        long loggedUserId = Long.valueOf(request.getSession().getAttribute("LOGGEDIN_USER_ID").toString());
        post.setAuthor(userService.findById(loggedUserId));

        postService.edit(post);
        notificationService.addInfoMessage("Post updated successfully!");

        return this.index(model,request);
    }

    @RequestMapping("/posts/delete/{id}")
    public String editPost(@PathVariable("id") Long id,HttpServletRequest request,Model model){
        LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        if(userData == null)
            return "redirect:/404";

        Post  post = postService.findById(id);

        long loggedUserId = Long.valueOf(request.getSession().getAttribute("LOGGEDIN_USER_ID").toString());
        if (!post.getAuthor().getId().equals(loggedUserId))
            return "redirect:/404";

        postService.deleteById(id);

        return this.myPosts(model,request);
    }
}
