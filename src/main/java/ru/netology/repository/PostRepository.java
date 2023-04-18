package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(0);

    public List<Post> all() {
        List<Post> allNotRemovedPosts = new ArrayList<>();
        Collection<Post> values = posts.values();
        for (Post post : values) {
            if (!post.isRemoved()) {
                allNotRemovedPosts.add(post);
            }
        }
        return allNotRemovedPosts;
    }

    public Optional<Post> getById(long id) {
        if (posts.containsKey(id) && !posts.get(id).isRemoved()) {
            return Optional.ofNullable(posts.get(id));
        } else {
            return Optional.empty();
        }
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long iterateId = idCounter.getAndIncrement();
            Post newPost = new Post(iterateId, post.getContent());
            posts.put(iterateId, newPost);
            return newPost;
        }
        Post targetPost = posts.get(post.getId());

        if (targetPost != null && !targetPost.isRemoved()) {
            targetPost.setContent(post.getContent());
            posts.put(post.getId(), targetPost);
        }
        return targetPost;
    }

    public boolean removeById(long id) {
        if (posts.containsKey(id)) {
            Post post = posts.get(id);
            post.setRemoved(true);
            return true;
        }
        return false;
    }

}
