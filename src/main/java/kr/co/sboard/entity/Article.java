package kr.co.sboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;


    private String cate;
    private String title;
    private String content;
    private int comment;
    private int file;
    private int hit;

    //private String writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User user;

    private String regip;

    @CreationTimestamp
    private LocalDateTime wdate;

    @OneToMany(mappedBy = "ano")   //mapped by 속성은 매핑되는 엔티티의 FK 컬럼
    private List<File> files;


    @PrePersist
    public void prePersist(){
        //엔티티 기본 속성 값 초기화
        if(this.cate == null){
            this.cate = "free";
        }
    }

}
