package kr.co.sboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private String cno;
    private int parent;
    private String content;
    private String writer;
    private String regip;
    private String wdate;

    private UserDTO user;


    public String getWdate(){
        if(wdate != null){
            return wdate.substring(0,10);
        }

        return null;
    }

}
