package com.study.springboot.domain.member;


import com.study.springboot.datas.KioskSession;
import com.study.springboot.domain.member.dto.UserDto;
import com.study.springboot.domain.member.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/member/list")
    public ResponseEntity<List<UserDto>> memberList(HttpSession session){
//        KioskSession userSession = (KioskSession) session.getAttribute("session");
//        UserRole role = userSession.getUserRole();
//
//        if(!role.equals(UserRole.ADMIN)){
//
//        }
        List<UserDto> userList = userService.findAll();

        return ResponseEntity.ok(userList);
    }
}
