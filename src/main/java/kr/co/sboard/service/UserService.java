package kr.co.sboard.service;

import kr.co.sboard.dto.UserDTO;
import kr.co.sboard.entity.User;
import kr.co.sboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입 정보 전송 + 비밀번호 암호화
    public void register(UserDTO userDTO){
        //엔티티 변환 전 pass 를 암호화
        String encodedPass = passwordEncoder.encode(userDTO.getPass());
        userDTO.setPass(encodedPass);

        //엔티티 변환
        User user = modelMapper.map(userDTO, User.class);

        //저장 : User savedUser = 를 하면 <select> 가 한 번 더 실행된다!
        userRepository.save(user);

    }

    //아이디 중복 체크
    public long checkUser(String type, String value){

        long count = 0;

        if(type.equals("uid")){
            count = userRepository.countByUid(value);
        }else if(type.equals("nick")){
            count = userRepository.countByNick(value);
        }else if(type.equals("email")){
            count = userRepository.countByEmail(value);
        }else if(type.equals("hp")){
            count = userRepository.countByHp(value);
        }

        return count;
    }


}
