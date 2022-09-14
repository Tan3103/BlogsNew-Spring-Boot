package com.tannur.weblog.controllers;
import com.tannur.weblog.model.Post;
import com.tannur.weblog.model.Role;
import com.tannur.weblog.model.User;
import com.tannur.weblog.repo.PostRepository;
import com.tannur.weblog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Collections;

@Controller
public class MainController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "main";
    }

    @GetMapping("/blog")
    public String blogAll(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-all";
    }

    @GetMapping("/blog-my")
    public String blogMy(@AuthenticationPrincipal User user, Model model){
        Iterable<Post> posts = postRepository.findAllByAuthor(user);


        model.addAttribute("posts", posts);
        return "blog-my";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
        User userFromDb = userRepository.findByEmail(user.getEmail());

        if(userFromDb != null){
            model.addAttribute("Message", "User exists!");
            return "registration";
        }

        user.setRoles(Collections.singleton(Role.ADMIN));
        userRepository.save(user);

        return "redirect:/login";
    }
}