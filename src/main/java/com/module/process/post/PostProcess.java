package com.module.process.post;

import com.module.cache.key.CacheKey;
import com.module.cache.annotation.CacheParam;
import com.module.core.annotation.JwtAuth;
import com.module.db.post.model.TbCommentDto;
import com.module.db.post.model.TbPostAllDto;
import com.module.db.post.model.TbPostDto;
import com.module.domain.post.rest.PostRest;
import com.module.process.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/v1/post")
public class PostProcess implements PostRest {

    @Autowired
    private PostService postService;

    @Override
    @JwtAuth
    public List<TbPostAllDto> findAllPostBySearch(Long userId, String search) {
        System.out.println("search : " + search);
        List<TbPostAllDto> posts = postService.findAllPostBySearch(userId,search);
        return posts;
    }

    @Override
    @JwtAuth
    @Cacheable(keyGenerator = CacheKey.POST_KEY_GENERATOR, value = CacheKey.POST)
    public TbPostDto findOnePostById(Long userId, @CacheParam Long postId) {
        TbPostDto postDto = postService.findOnePostById(userId, postId);
        return postDto;
    }

    @Override
    @JwtAuth
    public TbCommentDto findOneCommentById(Long userId, Long commentId) {
        TbCommentDto tbCommentDto = postService.findOneCommentById(userId, commentId);
        return tbCommentDto;
    }

    @Override
    @JwtAuth
    public Long insertPost(@RequestBody TbPostDto tbPostDto, Long userId) {
        Long postId = postService.insertPost(tbPostDto, userId);
        return postId;
    }

    @Override
    @JwtAuth
    @CachePut(keyGenerator = CacheKey.POST_KEY_GENERATOR, value = CacheKey.POST)
    public TbPostDto insertPostComment(Long userId, @CacheParam Long postId, Long commentId, @RequestBody TbPostDto tbPostDto) {
        return postService.insertPostComment(userId, postId, commentId, tbPostDto.getContent());
    }

    @Override
    @JwtAuth
    public Long insertCommentChildren(Long userId, Long commentId, TbCommentDto tbCommentDto) {
        return postService.insertCommentChildren(userId, commentId, tbCommentDto.getContent());
    }

    @Override
    @JwtAuth
    public Long insertPostLike(Long userId, Long postId) {
        postService.insertPostLike(userId, postId);
        return null;
    }

    @Override
    @JwtAuth
    public Long insertCommentLike(Long userId, Long commentId) {
        postService.insertCommentLike(userId, commentId);
        return null;
    }


}
