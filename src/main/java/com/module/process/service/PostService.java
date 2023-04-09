package com.module.process.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.module.core.exception.CommonException;
import com.module.db.common.model.PagingModel;
import com.module.db.post.entity.TbComment;
import com.module.db.post.entity.TbPost;
import com.module.db.post.entity.TbPostLike;
import com.module.db.post.model.*;
import com.module.db.user.entity.TbUser;
import com.module.domain.post.entityrepo.EPostRepo;
import com.module.domain.post.repo.CommentRepo;
import com.module.domain.post.repo.PostLikeRepo;
import com.module.domain.post.repo.PostRepo;
import com.module.domain.user.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.stream.events.Comment;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostService {

    @Autowired
    PostRepo postRepo;

    @Autowired
    PostLikeRepo postLikeRepo;

    @Autowired
    CommentRepo commentRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager em;

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

    public List<TbPostAllDto> findAllPost(Long userId) {
        TbUser tbUser = userRepo.findById(userId);
        List<TbPost> posts = postRepo.findAllPost();
        List<TbPostAllDto> postDtos = posts.stream().map(m -> modelMapper.map(m, TbPostAllDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    public List<TbPostAllDto> findAllPostBySearch(String search) {
        Optional<List<TbPostAllDto>> posts = postRepo.findAllPostBySearch(search);
        return posts.get();
    }

    public PagingModel<TbPostAllDto> findPostPagingBySearch(String search, Pageable pageable) {
        return postRepo.findPostPagingBySearch(search, pageable);
    }

    public TbPostDto findOnePostById(Long userId, Long postId) {
        System.out.println("postId : " + postId);
        System.out.println("userId : " + userId);

        TbPostDto postDto = postRepo.findOnePostById(postId).orElseThrow(() -> new CommonException("존재하지 않는 글 입니다."));
        Long countPostLike = postLikeRepo.countByPostId(postId);
        List<TbCommentDto> commentDtos = commentRepo.findCommentsByPostId(postId);

        List<TbCommentChildrenDto> commentChildrenDtos = commentRepo.findCommentChildrenByPostId(postId);

        commentDtos.forEach(parent -> parent.setChildren(commentChildrenDtos.stream()
                .filter(children -> children.getParentId().equals(parent.getCommentId()))
                .collect(Collectors.toList())));

        postDto.setComments(commentDtos);
        postDto.setCountPostLike(countPostLike);

        return postDto;


    }

    public TbPostDto insertPostComment(Long userId, Long postId, Long commentId, String content) {

        TbUser tbUser = userRepo.findById(userId);
        TbPost post = postRepo.findPost(postId);
        TbComment tbComment = TbComment.TbCommentBuilder()
                .tbPost(post)
                .tbUser(tbUser)
                .content(content).build();

        TbComment comment = commentRepo.insertPostComment(tbComment);

        if (commentId != 0) {
            TbComment tbCommentParent = commentRepo.findById(commentId);
            comment.updateParent(tbCommentParent);
        }

        return this.findOnePostById(userId, postId);
    }

    public TbCommentDto findOneCommentById(Long userId, Long commentId) {
        TbUser tbUser = userRepo.findById(userId);
        TbCommentDto tbCommentDto = commentRepo.findOneByCommentId(commentId)
                .orElseThrow(() -> new CommonException("존재하지 않는 댓글 입니다."));
        List<TbCommentChildrenDto> tbCommentChildrenDtos = commentRepo.findCommentChildrenByCommentId(commentId);

        tbCommentDto.setChildren(tbCommentChildrenDtos.stream()
                .filter(children -> children.getParentId().equals(tbCommentDto.getCommentId()))
                .collect(Collectors.toList()));
        return tbCommentDto;

    }

    public Long insertCommentChildren(Long userId, Long commentId, String content) {
        System.out.println("2323232323");
        TbUser tbUser = userRepo.findById(userId);
        TbComment comment = commentRepo.findById(commentId);

        TbComment childrenComment = TbComment.TbCommentBuilder()
                .content(content)
                .tbPost(comment.getTbPost())
                .tbUser(tbUser)
                .parent(comment)
                .build();
        TbComment newChildrenComment = commentRepo.insertPostComment(childrenComment);
        return commentId;
    }

    public void insertPostLike(Long userId, Long postId) {
        TbUser tbUser = userRepo.findById(userId);
        TbPost post = postRepo.findPost(postId);
    }

    public void insertCommentLike(Long userId, Long commentId) {
        TbUser tbUser = userRepo.findById(userId);
        TbComment comment = commentRepo.findById(commentId);
    }
}
