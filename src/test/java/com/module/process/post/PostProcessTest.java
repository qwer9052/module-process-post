package com.module.process.post;

import com.module.core.aspect.JwtAspect;
import com.module.core.jwt.JwtProvider;
import com.module.db.post.model.TbPostDto;
import com.module.process.service.PostService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostProcess.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
class PostProcessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostService postService;

//    @Mock
//    private ProceedingJoinPoint proceedingJoinPoint;
//
//    @Mock
//    private HttpServletRequest httpServletRequest;
//
//    @Mock
//    private JwtProvider jwtProvider;
//
//    private JwtAspect jwtAspect = new JwtAspect(httpServletRequest, jwtProvider);


    @Test
    @DisplayName("Post one detail Test1")
    void getPostDetail() throws Throwable {
        TbPostDto dto = new TbPostDto();
        dto.setPostId(1L);

        //jwtAspect.jwt(proceedingJoinPoint);

        given(postService.findOnePostById(1L, 2L)).willReturn(dto);

        mockMvc.perform(
                        get("http://localhost:8100/v1/post/" + 2 + "?userId=" + 1)
                        //.contentType(MediaType.APPLICATION_JSON)
                        //.header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY3MDA5MDk2MywiZXhwIjoxNjcwMDkyNzYzfQ.C379OYznjVejGOp21AaxdHRMTEnCax2cwbnWhjhJAQE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").exists())
                .andExpect(handler().handlerType(PostProcess.class))
                .andExpect(handler().methodName("findOnePostById"))
                .andDo(print())
        ;

        //.andExpect(jsonPath("$.postId").exists());

        verify(postService, times(1)).findOnePostById(1L, 2L);
    }


}
