package kr.co.sboard.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.sboard.dto.PageRequestDTO;
import kr.co.sboard.entity.QArticle;
import kr.co.sboard.entity.QUser;
import kr.co.sboard.repository.custom.ArticleRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {


    private final JPAQueryFactory queryFactory;
    private QArticle qArticle = QArticle.article;
    private QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllForList(Pageable pageable) {
        List<Tuple> tupleList = queryFactory
                                        .select(qArticle, qUser.nick)
                                        .from(qArticle)
                                        .join(qUser)
                                        .on(qArticle.user.uid.eq(qUser.uid))
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .orderBy(qArticle.no.desc())
                                        .fetch();

        long total = queryFactory.select(qArticle.count()).from(qArticle).fetchOne();


        //페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectAllForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        //검색 조건에 따라서 where 조건 표현식을 생성해야 함!
        BooleanExpression expression = null;
        if(searchType.equals("title")){
            expression = qArticle.title.contains(keyword);
        }else if(searchType.equals("content")){
            expression = qArticle.content.contains(keyword);
        }else if(searchType.equals("wrtier")){
            expression = qArticle.user.uid.contains(keyword);
        }

        List<Tuple> tupleList = queryFactory
                .select(qArticle, qUser.nick)
                .from(qArticle)
                .join(qUser)
                .on(qArticle.user.uid.eq(qUser.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qArticle.no.desc())
                .fetch();

        long total = queryFactory
                .select(qArticle.count())
                .from(qArticle)
                .join(qUser)  //Nick 정보를 위해서 Join 필요!
                .on(qArticle.user.uid.eq(qUser.uid))
                .where(expression)
                .fetchOne();

        //페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }
}
