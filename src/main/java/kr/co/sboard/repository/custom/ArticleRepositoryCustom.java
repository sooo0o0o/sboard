package kr.co.sboard.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.sboard.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {

    public Page<Tuple> selectAllForList(Pageable pageable);
    public Page<Tuple> selectAllForSearch(PageRequestDTO pageRequestDTO,Pageable pageable);

}
