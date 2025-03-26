package kr.co.sboard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        //로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?code=100")
                .usernameParameter("uid")       //login.html 에 입력데는 id 데이터 이름
                .passwordParameter("pass"));    //login.html 에 입력데는 password 데이터 이름


        //로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/user/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/user/login?code=101"));
        /*
            인가 설정
                - MyUserDetails 권한 목록 생성하는 메서드(getAuthorities)에서
                접두어로 ROLE_ 입력해야 hasRole, hasAnyRole 권한처리가 가능함
                - Sprint Security 는 기본적으로 존재하지 않는 요청 주소에 대해 login 페이지로 redirect 시킴
        */


        //인가 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/")
                .permitAll()  // -> 로그인 하던 하지 않던 index("/") 에 접근 할 수 있다!
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/staff/**").hasAnyRole("ADMIN","MANAGER", "STAFF")
                .requestMatchers("/article/**").authenticated()
                .requestMatchers("/user/**").permitAll()
                .anyRequest()
                .permitAll());

        http.exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, AccessDeniedException) -> response
                        .sendRedirect("/")));

        //기타 보안 설정              :: -> 람다식, 메소드 참조 문법
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //security 암호화 인코더 설정
        return new BCryptPasswordEncoder();
    }




}
