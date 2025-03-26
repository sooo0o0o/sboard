package kr.co.sboard.service;

import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.FileDTO;
import kr.co.sboard.entity.File;
import kr.co.sboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public void save(FileDTO fileDTO) {

        File file = modelMapper.map(fileDTO, File.class);

        fileRepository.save(file);

    }

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    public List<FileDTO> uploadFile(ArticleDTO articleDTO) {
        //파일 업로드 경로(디렉토리) 객체 생성
        //File 이름이 중복으로 겹쳐서 java.io 는 직접 언급하여 정의해준것
        java.io.File fileUploadDir = new java.io.File(uploadDir);

        if (!fileUploadDir.exists()) {
            //디렉토리 존재하지않을시 생성
            fileUploadDir.mkdirs();
        }

        //파일 업로드 디렉터리 시스템 경로 구하기
        String fileUploadPath = fileUploadDir.getAbsolutePath();
        log.info("fileUploadPath: {}", fileUploadPath);

        //파일 정보 객체 가져오기 (From DTO)
        List<MultipartFile> multipartFiles = articleDTO.getMultipartFiles();

        //업로드 파일 정보 객체 생성(반환용)
        List<FileDTO> fileDTOList = new ArrayList<>();


        for(MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                //파일을 첨부했을 때 실행
                String oName = multipartFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String sName = UUID.randomUUID().toString() + ext;

                //파일 저장
                try {
                    multipartFile.transferTo(new java.io.File(fileUploadPath, sName));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }

                //반환용 객체 생성
                FileDTO fileDTO = FileDTO
                        .builder()
                        .oName(oName)
                        .sName(sName)
                        .build();

                fileDTOList.add(fileDTO);
            }
        }

        return fileDTOList;
    }



    public void downloadFile(){}


}
