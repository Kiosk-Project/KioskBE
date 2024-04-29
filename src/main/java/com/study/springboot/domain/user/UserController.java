package com.study.springboot.domain.user;


import com.study.springboot.datas.KioskSession;
import com.study.springboot.datas.Message;
import com.study.springboot.domain.user.dto.RequestAddUserDto;
import com.study.springboot.domain.user.dto.RequestLoginDto;
import com.study.springboot.domain.user.service.UserService;
import com.study.springboot.enumeration.error.StatusCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Message> userLogin(@RequestBody RequestLoginDto dto, HttpSession session){
        String userId = dto.getUserId();
        String userPw = dto.getUserPw();

        Message message = userService.setLoginMessage(userId, userPw);

        KioskSession kioskSession = null;

        //세션 setting
        //응답 결과가 '관리자 로그인 성공'
        if(message.getStatus().equals(StatusCode.ADMIN_LOGIN)){
            kioskSession = KioskSession.makeAdminSession(userId);
        }

        //응답 결과가 '유저 로그인 성공'
        else if(message.getMessage().equals(StatusCode.USER_LOGIN)){
            kioskSession = KioskSession.makeUserSession(userId);
        }

        session.setAttribute("sesseion", kioskSession);

        return ResponseEntity.ok(message);
    }




    @PostMapping("/register")
    public ResponseEntity<Message> addUser(@RequestBody RequestAddUserDto dto){

        String userId = dto.getUserId();
        String userPw = dto.getUserPw();
        String userName = dto.getUserName();

        Message message = userService.setUserRegisterMessage(userId, userPw, userName);

        return ResponseEntity.ok(message);
    }
}