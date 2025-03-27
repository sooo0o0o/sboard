package kr.co.sboard.dto;

import kr.co.sboard.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDTO {

    private int no;
    private String cate;
    private String title;
    private String content;
    private int comment;
    private int file;
    private int hit;
    private String writer;
    private String regip;
    private String wdate;

    public String getWdate(){
        return wdate.substring(0,10);   // yyyy-mm-dd
    }

    //추가필드
    private String nick;

    private UserDTO user;
    private List<FileDTO> files;


    //form 에서 입력하는 file -> 첨부파일 객체
    private MultipartFile file1;
    private MultipartFile file2;

    public List<MultipartFile> getMultipartFiles(){
        return List.of(file1, file2);
    }

}
