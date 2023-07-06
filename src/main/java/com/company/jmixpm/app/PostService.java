package com.company.jmixpm.app;

import com.company.jmixpm.entity.Post;
import com.company.jmixpm.entity.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PostService {


    public List<Post> fetchPosts() {
        RestTemplate restTemplate = new RestTemplate();
        Post[] posts = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts", Post[].class);

        return posts != null ? Arrays.asList(posts) : Collections.emptyList();
    }

    public List<Post> fetchPosts(int first, int max) {
        RestTemplate restTemplate = new RestTemplate();
        Post[] posts = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts?_start={start}&_end={end}", Post[].class,
                first, first + max);

        return posts != null ? Arrays.asList(posts) : Collections.emptyList();
    }

    public UserInfo fetchUserInfo(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("https://jsonplaceholder.typicode.com/users/{id}", UserInfo.class, id);
    }

    public int postsTotalCount() {
        return fetchPosts().size();
    }
}