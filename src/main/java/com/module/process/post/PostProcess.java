package com.module.process.post;

import com.module.core.annotation.JwtAuth;
import com.module.db.post.model.TbPostDto;
import com.module.domain.post.rest.PostRest;
import com.module.process.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/post")
public class PostProcess implements PostRest {

    @Autowired
    private PostService postService;

    @Override
    @JwtAuth
    public List<TbPostDto> findAllPost(Long userId) {
        List<TbPostDto> posts = postService.findAllPost(userId);
        return posts;
    }

    @Override
    @JwtAuth
    public TbPostDto findPost(Long userId, Long postId) {
        TbPostDto postDto = postService.findPost(userId, postId);
        return postDto;
    }

    @Override
    @JwtAuth
    public Long insertPost(@RequestBody TbPostDto tbPostDto, Long userId) {
        Long postId = postService.insertPost(tbPostDto, userId);
        return postId;
    }


}
