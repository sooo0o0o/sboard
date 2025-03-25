package kr.co.sboard.security;

import kr.co.sboard.entity.User;
import kr.co.sboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
         * AuthenticationProvider 에서 DB 에 SQL 실행 -> DB 에서 username 전송 (이 단계에서 인증은 끝남)
         * (sql : select ?? from `USER` where `uid`=? and `password`=?)
         * 전송받은 username 을 UserDetailsService 에서 호출해서 사용
         * (이미 인증이 끝났으므로, id(=username) 만 가지고 유저 조회)
         */
        log.info("username : {}", username);

        //사용자 조회 : 사용자가 입력한 아이디, 비밀번호는 이전단계인 AuthenticationProvider 에서 이미 수행됨
        //optional = null 체크 강제를 위함
        Optional<User> optUser = userRepository.findById(username);

        if (optUser.isPresent()) {
            // Security  사용자 인증객체 생성
            MyUserDetails myUserDetails = MyUserDetails.builder()
                    .user(optUser.get())
                    .build();

            // 리턴되는 myUserDetails 는 Security ContextHolder 에 저장
            return myUserDetails;
        }



        return null;
    }
}
