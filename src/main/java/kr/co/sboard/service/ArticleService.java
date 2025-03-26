package kr.co.sboard.service;

import kr.co.sboard.dao.ArticleMapper;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ArticleService {

    /*
    @Component 가 하는 역할
        @Autowired
        public ArticleService(ArticleRepository repository){
        ....}
    */

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ModelMapper modelMapper;


    public int register(ArticleDTO articleDTO) {

        //엔티티 변환
        Article article = modelMapper.map(articleDTO, Article.class);
        log.info("article : {}", article);

        //MyBatis 저장
        articleMapper.insertArticle(articleDTO);

        //매개변수로 전달되는 articleDTO 의 no 속성에 mybatis 가 insert 한 데이터의 pk 값을 반환
        int no = articleDTO.getNo();

        return no;


        //JPA 저장
        //Article savedArticle = articleRepository.save(article);
        //저장한 글 번호 반환
        //return savedArticle.getNo();


    }
}
