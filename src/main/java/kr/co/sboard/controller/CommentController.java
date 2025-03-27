package kr.co.sboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sboard.dto.CommentDTO;
import kr.co.sboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    //Model model = return 할 값이 page 인 경우 사용

    @ResponseBody
    @GetMapping("/comment/list")
    public List<CommentDTO> list(int parent){
        log.info("parent : {}", parent);

        List<CommentDTO> commentDTOList = commentService.findByParent(parent);

        return commentDTOList;
    }


    @ResponseBody
    @PostMapping("/comment/write")
    public CommentDTO write(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        log.info("commentDTO : {}", commentDTO);

        String regip = request.getRemoteAddr();
        commentDTO.setRegip(regip);

        CommentDTO savedCommentDTO = commentService.save(commentDTO);


        return savedCommentDTO;

    }






}
