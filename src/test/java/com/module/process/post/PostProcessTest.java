package com.module.process.post;

import com.module.core.aspect.JwtAspect;
import com.module.core.jwt.JwtProvider;
import com.module.db.post.entity.TbPost;
import com.module.db.post.enums.PostType;
import com.module.db.post.model.TbPostDto;
import com.module.process.service.PostService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(PostProcess.class)
//@MockBean(JpaMetamodelMappingContext.class)
//@AutoConfigureMockMvc
class PostProcessTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    PostService postService;
//
////    @Mock
////    private ProceedingJoinPoint proceedingJoinPoint;
////
////    @Mock
////    private HttpServletRequest httpServletRequest;
////
////    @Mock
////    private JwtProvider jwtProvider;
////
////    private JwtAspect jwtAspect = new JwtAspect(httpServletRequest, jwtProvider);
//
//
//    @Test
//    @DisplayName("Post one detail Test1")
//        //테스트 이름
//
//        //@RepeatedTest(10) //테스트 반복시 사용
//        //@RepeatedTest(value = 10, name="{displayName}")
//
//        //@ParameterizedTest // 테스트에 여러 다른 매개변수를 대입해가며 반복 실행할 때 사용하는 어노테이션
//        //@CsvSource(value = {"ACE,ACE:12", "ACE,ACE,ACE:13", "ACE,ACE,TEN:12"}, delimiter = ':')
//
//        //@Nested // 테스트 클래스 안에서 내부 클래스를 정의해 테스트를 계층화 할때 사용
//
//    void getPostDetail() throws Throwable {
//        TbPostDto dto = new TbPostDto();
//        dto.setPostId(1L);
//
//        //jwtAspect.jwt(proceedingJoinPoint);
//
//        given(postService.findOnePostById(1L, 2L)).willReturn(dto);
//
//        mockMvc.perform(
//                        get("http://localhost:8100/v1/post/" + 2 + "?userId=" + 1)
//                        //.contentType(MediaType.APPLICATION_JSON)
//                        //.header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY3MDA5MDk2MywiZXhwIjoxNjcwMDkyNzYzfQ.C379OYznjVejGOp21AaxdHRMTEnCax2cwbnWhjhJAQE")
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.postId").exists())
//                .andExpect(handler().handlerType(PostProcess.class))
//                .andExpect(handler().methodName("findOnePostById"))
//                .andDo(print())
//        ;
//
//        //.andExpect(jsonPath("$.postId").exists());
//
//        verify(postService, times(1)).findOnePostById(1L, 2L);
//    }
//
//    @Test
//    void test() {
//        TbPostDto postDto = postService.findOnePostById(1L, 2L);
//
//        assertNotNull(postDto);
//        assertEquals(postDto.getPostType().getName(), PostType.FREE, "테스트 중");
//        assertTrue(postDto.getPostId() > 0, () -> "0보다 커야함");
//
//        assertAll(
//                () -> assertNotNull(postDto),
//                () -> assertEquals(postDto.getPostType().getName(), PostType.FREE, "테스트 중"),
//                () -> assertTrue(postDto.getPostId() > 0, () -> "0보다 커야함")
//        );
//    }


}
