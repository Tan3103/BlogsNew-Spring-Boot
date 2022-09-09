package com.tannur.weblog.repo;

import com.tannur.weblog.model.Post;
import com.tannur.weblog.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findAllByAuthor(User user);

    List<Post> findByTitle(String title);
}
