package kr.co.sboard.dao;

import kr.co.sboard.dto.ArticleDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {

    //Mybatis 가 Insert 한 데이터의 pk 값을 매개변수 DTO 의 해당 속성으로 반환
    public void insertArticle(ArticleDTO articleDTO);
}
