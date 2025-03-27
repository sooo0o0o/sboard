package kr.co.sboard.service;

import com.querydsl.core.Tuple;
import kr.co.sboard.dao.ArticleMapper;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.PageRequestDTO;
import kr.co.sboard.dto.PageResponseDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.entity.User;
import kr.co.sboard.repository.ArticleRepository;
import kr.co.sboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
@Component 가 하는 역할
    @Autowired
    public ArticleService(ArticleRepository repository){
    ....}
*/
@Slf4j
@RequiredArgsConstructor
@Component
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ModelMapper modelMapper;
    private final FileRepository fileRepository;

    public PageResponseDTO searchAll(PageRequestDTO pageRequestDTO) {

        //페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageArticle = articleRepository.selectAllForSearch(pageRequestDTO, pageable);
        log.info("pageArticle: {}", pageArticle);

        //Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<ArticleDTO> articleDTOList = pageArticle.getContent().stream().map(tuple -> {
            Article article = tuple.get(0, Article.class);
            String nick = tuple.get(1, String.class);

            ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
            articleDTO.setNick(nick);

            return articleDTO;

        }).toList();

        int total = (int) pageArticle.getTotalElements();   //전체 게시물 갯수

        return PageResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(articleDTOList)
                .total(total)
                .build();
    }

    public PageResponseDTO findAll(PageRequestDTO pageRequestDTO) {

        //페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageArticle = articleRepository.selectAllForList(pageable);
        log.info("pageArticle: {}", pageArticle);

        //Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<ArticleDTO> articleDTOList = pageArticle.getContent().stream().map(tuple -> {
            Article article = tuple.get(0, Article.class);
            String nick = tuple.get(1, String.class);

            ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
            articleDTO.setNick(nick);

            return articleDTO;

        }).toList();

        int total = (int) pageArticle.getTotalElements();   //전체 게시물 갯수

        return PageResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(articleDTOList)
                .total(total)
                .build();
    }

    public ArticleDTO findById(int no) {
        Optional<Article> optArticle = articleRepository.findById(no);
        if (optArticle.isPresent()) {
            Article article = optArticle.get();

            ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);

            return articleDTO;
        }

        return null;
    }

    public int register(ArticleDTO articleDTO) {

        //엔티티 변환
        User user = User.builder()
                .uid(articleDTO.getWriter())
                .build();

        Article article = modelMapper.map(articleDTO, Article.class);
        article.setUser(user);

        log.info("article : {}", article);

        //MyBatis 저장
        //articleMapper.insertArticle(articleDTO);

        //매개변수로 전달되는 articleDTO 의 no 속성에 mybatis 가 insert 한 데이터의 pk 값을 반환
        //int no = articleDTO.getNo();

        //return no;


        //JPA 저장
        Article savedArticle = articleRepository.save(article);
        //저장한 글 번호 반환
        return savedArticle.getNo();


    }

    public void modify(ArticleDTO articleDTO) {

        Article article = articleRepository.findById(articleDTO.getNo()).get();

        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        log.info("article : {}", article);


        articleRepository.save(article);

        System.out.println("데이터베이스에 수정된 값 저장됨: " + article.getTitle());

    }

    @Transactional
    public void delete(int no) {
        //FK로 인해 파일을 먼저 삭제하고, 그 후에 Article 을 삭제
        fileRepository.deleteByAno(no);

        articleRepository.deleteById(no);
    }


}
