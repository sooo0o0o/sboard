package kr.co.sboard.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sboard.dto.UserDTO;
import kr.co.sboard.entity.User;
import kr.co.sboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final HttpServletRequest req;

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

            if(count == 0){
                String code = sendEmailCode(value);

                //인증코드를 비교하기 위해서 세션을 저장

                HttpSession session = req.getSession();
                session.setAttribute("authCode", code);
            }


        }else if(type.equals("hp")){
            count = userRepository.countByHp(value);
        }

        return count;
    }

    @Value("${spring.mail.username}")
    private String sender;

    public String sendEmailCode(String receiver){
        //MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        //인증코드 생성
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        log.info("code >> " + code);
        String subject = "sboard 인증코드 안내";
        String content = "<h1>sboard 인증코드는 " + code  + " 입니다.</h1>";

        try{

            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject("subject");
            message.setContent(content, "text/html; charset=utf-8");

            //메일 발송
            mailSender.send(message);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return String.valueOf(code);

    }


}
