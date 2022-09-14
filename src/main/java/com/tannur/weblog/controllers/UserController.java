package com.tannur.weblog.controllers;

import com.tannur.weblog.model.Post;
import com.tannur.weblog.model.User;
import com.tannur.weblog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    String password = "";

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());

        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);

        password = user.getPassword();

        return "userEdit";
    }

    @PostMapping("{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        user.setPassword(password);

        userRepository.save(user);
        return "redirect:/user";
    }
}
