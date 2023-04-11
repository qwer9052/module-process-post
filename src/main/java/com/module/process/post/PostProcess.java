package com.module.process.post;

import com.module.cache.annotation.CacheParam;
import com.module.cache.key.CacheKey;
import com.module.core.annotation.JwtAcceptableAuth;
import com.module.core.annotation.JwtAuth;
import com.module.db.common.enums.Del;
import com.module.db.common.model.PagingModel;
import com.module.db.post.model.TbCommentDto;
import com.module.db.post.model.TbPostAllDto;
import com.module.db.post.model.TbPostDto;
import com.module.domain.post.rest.PostRest;
import com.module.process.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/post")
@Slf4j
public class PostProcess implements PostRest {

    @Autowired
    private PostService postService;

    @Override
    public PagingModel<TbPostAllDto> findAllPostBySearch(String search, Pageable pageable) {
        return postService.findPostPagingBySearch(search, pageable);
    }

    @Override
    @JwtAcceptableAuth
    //@Cacheable(keyGenerator = CacheKey.POST_KEY_GENERATOR, value = CacheKey.POST)
    //@CacheParam
    public TbPostDto findOnePostById(Long userId, Long postId) {
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
        return postService.insertPost(tbPostDto, userId);
    }

    @Override
    @JwtAuth
    //@CachePut(keyGenerator = CacheKey.POST_KEY_GENERATOR, value = CacheKey.POST)
    //@CacheParam
    public TbPostDto insertPostComment(Long userId, Long postId, Long commentId, @RequestBody TbPostDto tbPostDto) {
        return postService.insertPostComment(userId, postId, commentId, tbPostDto.getContent());
    }

    @Override
    @JwtAuth
    public Long insertCommentChildren(Long userId, Long commentId, @RequestBody TbCommentDto tbCommentDto) {
        return postService.insertCommentChildren(userId, commentId, tbCommentDto.getContent());
    }

    @Override
    @JwtAuth
    public Del insertPostLike(Long userId, Long postId) {
        return postService.insertPostLike(userId, postId);
    }

    @Override
    @JwtAuth
    public Del insertCommentLike(Long userId, Long commentId) {
        return postService.insertCommentLike(userId, commentId);
    }

    @Override
    public void insertHistory(Long postId, HttpServletRequest request, HttpServletResponse response) {
        log.info("[postHistory] : " + postId);
        Cookie[] cookies = request.getCookies();


        if (cookies == null || Stream.of(cookies)
                .noneMatch(cookie -> cookie.getName().equals("postId") && cookie.getValue().equals(String.valueOf(postId)))) {
            postService.insertPostHistory(postId);
            Cookie cookie = new Cookie("postId", String.valueOf(postId));
            cookie.setMaxAge(60 * 60);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }


}
