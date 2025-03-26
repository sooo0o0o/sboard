package kr.co.sboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.FileDTO;
import kr.co.sboard.service.ArticleService;
import kr.co.sboard.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;

    @GetMapping("/article/list")
    public String list(){
        return "/article/list";
    }

    @GetMapping("/article/modify")
    public String modify(){
        return "/article/modify";
    }

    @GetMapping("/article/view")
    public String view(){
        return "/article/view";
    }

    @GetMapping("/article/write")
    public String write(){
        return "/article/write";
    }
    @PostMapping("/article/write")
    public String write(ArticleDTO articleDTO, HttpServletRequest request){
        log.info("articleDTO: {}", articleDTO);

        String regip = request.getRemoteAddr();
        articleDTO.setRegip(regip);

        //파일 업로드 서비스 호출
        List<FileDTO> files = fileService.uploadFile(articleDTO);

        //글 저장 서비스 호출
        articleDTO.setFile(files.size());
        int no = articleService.register(articleDTO);

        //ano = articleNo 이므로, 글 저장 후에 첨부된 파일을 다운로드
        //파일 저장 서비스 호출

        for(FileDTO fileDTO : files) {
            fileDTO.setAno(no);
            fileService.save(fileDTO);
        }
        //리다이렉트
        return "redirect:/article/list";
    }

    @GetMapping("/article/searchList")
    public String searchList(){
        return "/article/searchList";
    }

}
