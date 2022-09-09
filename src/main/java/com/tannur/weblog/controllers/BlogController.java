package com.tannur.weblog.controllers;

import com.tannur.weblog.model.Post;
import com.tannur.weblog.model.User;
import com.tannur.weblog.repo.PostRepository;
import com.tannur.weblog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@AuthenticationPrincipal User user, @RequestParam String title, @RequestParam String full_text, Model model) {
        Post post = new Post(title, full_text, user);
        postRepository.save(post);
        return "redirect:/blog";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*@GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("posts", res);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@RequestParam long id,@RequestParam String title, @RequestParam String full_text, Model model){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setFull_text(full_text);
        post.setAuthor(post.getAuthor());
        postRepository.save(post);
        return "redirect:/blog";
    }*/

    @GetMapping("/blog/{id}/edit")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("post", post);
        return "blog-edit";
    }

    @PostMapping("/blog/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Post post,
                             BindingResult result, Model model, @AuthenticationPrincipal User user) {
        if (result.hasErrors()) {
            post.setId(id);
            return "update-user";
        }

        post.setAuthor(user);

        postRepository.save(post);
        return "redirect:/blog";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);

        return "redirect:/blog";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/blog/{id}")
    public String blogDetailsMy(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details-my";
    }

    @GetMapping("/blog/all/{id}")
    public String blogDetailsAll(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details-all";
    }

    @GetMapping
    public String main(Model model) {
        Iterable<Post> posts = postRepository.findAll();

        model.addAttribute("posts", posts);

        return "main";
    }

    @PostMapping("/fil")
    public String filterBlogs(@RequestParam String fil, Model model) {
        List<Post> posts = postRepository.findByTitle(fil);

        model.addAttribute("posts", posts);

        return "main";
    }
}
