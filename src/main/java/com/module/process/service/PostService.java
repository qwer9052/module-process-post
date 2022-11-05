package com.module.process.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.module.db.post.entity.TbPost;
import com.module.db.user.entity.TbUser;
import com.module.db.post.model.TbPostDto;
import com.module.domain.post.repo.PostRepo;
import com.module.domain.user.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostService {

    @Autowired
    PostRepo postRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ModelMapper modelMapper;

    public TbPostDto of(TbPost tbPost) {
        return modelMapper.map(tbPost, TbPostDto.class);
    }

    public Long insertPost(TbPostDto tbPostDto, Long userId) {
        TbUser tbUser = userRepo.findById(userId);
        TbPost tbPost = TbPost.TbPostBuilder()
                .title(tbPostDto.getTitle())
                .content(tbPostDto.getContent())
                .tbUser(tbUser)
                .build();
        Long postId = postRepo.insertPost(tbPost);
        return postId;
    }

    public List<TbPostDto> findAllPost(Long userId) {
        TbUser tbUser = userRepo.findById(userId);
        List<TbPost> posts = postRepo.findAllPost();
        List<TbPostDto> postDtos = posts.stream().map(this::of).collect(Collectors.toList());
        return postDtos;
    }

    public TbPostDto findPost(Long userId, Long postId) {
        TbUser tbUser = userRepo.findById(userId);
        TbPost post = postRepo.findPost(postId);
        TbPostDto postDtos = this.of(post);
        return postDtos;
    }
}
