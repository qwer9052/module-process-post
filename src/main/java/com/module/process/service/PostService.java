package com.module.process.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.module.core.exception.CommonException;
import com.module.db.common.enums.Del;
import com.module.db.common.model.PagingModel;
import com.module.db.post.entity.*;
import com.module.db.post.enums.PostType;
import com.module.db.post.model.*;
import com.module.db.user.entity.TbUser;
import com.module.domain.post.repo.*;
import com.module.domain.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
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

    @Autowired
    CommentLikeRepo commentLikeRepo;

    @Autowired
    PostHistoryRepo postHistoryRepo;


    @PersistenceContext
    private EntityManager em;

    public TbPostDto of(TbPost tbPost) {
        return modelMapper.map(tbPost, TbPostDto.class);
    }

    public TbPostLikeDto of(TbPostLike tbPostLike) {
        return modelMapper.map(tbPostLike, TbPostLikeDto.class);
    }

    public Long insertPost(TbPostDto tbPostDto, Long userId) {
        TbUser tbUser = userRepo.findById(userId);
        TbPost tbPost = TbPost.TbPostBuilder()
                .title(tbPostDto.getTitle())
                .content(tbPostDto.getContent())
                .tbUser(tbUser)
                .postType(tbPostDto.getPostType())
                .build();
        TbPost result = postRepo.insertPost(tbPost);

        TbPostHistory tbPostHistory = TbPostHistory.TbPostHistoryBuilder()
                .tbPost(result)
                .build();

        TbPostHistory resultHistory = postHistoryRepo.insertPostHistory(tbPostHistory);

        result.setTbPostHistory(resultHistory);
        postRepo.insertPost(result);

        return result.getPostId();
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

    public TbPostDto findOnePostByIdAndUser(Long userId, Long postId) {

        TbPostDto postDto = postRepo.findOnePostById(postId).orElseThrow(() -> new CommonException("존재하지 않는 글 입니다."));
        Long countPostLike = postLikeRepo.countByPostId(postId);
        Long countHistory = postHistoryRepo.countByPostId(postId);
        List<TbCommentDto> commentDtos = commentRepo.findCommentsByPostIdAndUserId(postId);
        List<TbCommentChildrenDto> commentChildrenDtos = commentRepo.findCommentChildrenByPostIdAndUserId(postId);

        commentDtos.forEach(parent -> parent.setChildren(commentChildrenDtos.stream()
                .filter(children -> children.getParentId().equals(parent.getCommentId()))
                .collect(Collectors.toList())));

        postDto.setComments(commentDtos);
        postDto.setCountPostLike(countPostLike);
        postDto.setCountHistory(countHistory);
        if (userId != null) {
            TbUser tbUser = userRepo.findById(userId);
            postDto.setLike(postLikeRepo.findBooleanByPostAndUser(tbUser, postId));
        }
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

    public Del insertPostLike(Long userId, Long postId) {

        Del del = Del.N;
        TbUser tbUser = userRepo.findById(userId);
        TbPost tbPost = postRepo.findPost(postId);

        Optional<TbPostLike> tbPostLike = postLikeRepo.findByPostAndUser(tbUser, postId);

        if (tbPostLike.isPresent()) {
            del = Del.valueChange(tbPostLike.get().getDel());
            tbPostLike.get().setDel(del);
        } else {
            tbPostLike = Optional.ofNullable(TbPostLike.TbPostLikeBuilder()
                    .tbUser(tbUser)
                    .tbPost(tbPost)
                    .build());
            postLikeRepo.insertPostLike(tbPostLike.get());
        }
        return del;

    }

    public Del insertCommentLike(Long userId, Long commentId) {

        Del del = Del.N;
        TbUser tbUser = userRepo.findById(userId);
        TbComment tbComment = commentRepo.findById(commentId);

        Optional<TbCommentLike> tbCommentLike = commentLikeRepo.findByCommentAndUserAndDel(tbUser, commentId);

        //Optional<TbPostLike> tbPostLike = postLikeRepo.findByPostAndUserAndDel(tbUser, postId);

        if (tbCommentLike.isPresent()) {
            System.out.println("commentId : " + 11111);
            del = Del.valueChange(tbCommentLike.get().getDel());
            tbCommentLike.get().setDel(del);
        } else {
            System.out.println("commentId : " + 22222);
            tbCommentLike = Optional.ofNullable(TbCommentLike.TbPostLikeBuilder()
                    .tbUser(tbUser)
                    .tbComment(tbComment)
                    .build());
            commentLikeRepo.insertCommentLike(tbCommentLike.get());
        }
        return del;
    }

    public void insertPostHistory(Long postId) {
        TbPost tbPost = postRepo.findPost(postId);
        Optional<TbPostHistory> tbPostHistory = postHistoryRepo.findByTbPost(tbPost);

        tbPostHistory.ifPresentOrElse(
                TbPostHistory::addCount,
                () -> postHistoryRepo.insertPostHistory(TbPostHistory.TbPostHistoryBuilder()
                        .tbPost(tbPost)
                        .build()));


    }

    public PagingModel<TbPostAllDto> findAllPostByPostType(PostType postType, Pageable pageable) {
        return postRepo.findAllPostByPostType(postType, pageable);
    }
}
