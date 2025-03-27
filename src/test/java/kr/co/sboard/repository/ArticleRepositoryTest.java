package kr.co.sboard.repository;

import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;


    @DisplayName("관계설정 테스트")
    @Transactional
    @Test
    public void test1(){
        //given
        int no = 11;

        //when
        Article article = articleRepository.findById(no).get();

        //then
        System.out.println(article);
    }

    @DisplayName("서비스 테스트")
    @Transactional
    @Test
    public void test2(){
        ArticleDTO articleDTO = articleService.findById(11);
        System.out.println(articleDTO);
    }
}