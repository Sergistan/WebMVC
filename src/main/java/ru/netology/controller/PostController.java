package ru.netology.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.netology.dto.PostDto;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;
    private final ModelMapper modelMapper;

    @Autowired
    public PostController(PostService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PostDto> all() {
        return service.all().stream().map(this::convertToPersonDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostDto getById(@PathVariable long id) {
        return convertToPersonDto(service.getById(id));
    }

    @PostMapping
    public Post save(@RequestBody PostDto postDto) {
        return service.save(convertToPerson(postDto));
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }

    private Post convertToPerson(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }

    private PostDto convertToPersonDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }
}

