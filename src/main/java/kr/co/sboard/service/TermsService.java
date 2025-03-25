package kr.co.sboard.service;

import kr.co.sboard.dto.TermsDTO;
import kr.co.sboard.entity.Terms;
import kr.co.sboard.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service    //class 라서 객체로 생성해서 사용하는 것 => 컨테이너에 등록하기위해서 어노테이션 사용
public class TermsService {

    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;

    public TermsDTO terms(){
        Optional<Terms> optTerms = termsRepository.findById(1L);
        if(optTerms.isPresent()){
            Terms terms = optTerms.get();

            //modelMapper 를 이용한 변환
            TermsDTO termsDTO = modelMapper.map(terms, TermsDTO.class);
            return termsDTO;

        }
        return null;

    }


}
