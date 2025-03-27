package kr.co.sboard.repository;

import kr.co.sboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {


    public List<Comment> findByParent(int parent);
}