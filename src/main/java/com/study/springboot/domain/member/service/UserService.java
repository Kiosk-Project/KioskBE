package com.study.springboot.domain.member.service;


import com.study.springboot.datas.KioskSession;
import com.study.springboot.domain.member.User;
import com.study.springboot.domain.member.dto.RequestAddUserDto;
import com.study.springboot.domain.member.dto.RequestEditUserDto;
import com.study.springboot.domain.member.dto.UserDto;
import com.study.springboot.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public List<UserDto> findAll(){

        //TODO: 정렬기능
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }


    @Transactional
    public User addUser(RequestAddUserDto dto){
        User user = userRepository.save(dto.toUserEntity());

        try{
            if(user == null) throw new RuntimeException("멤버 추가 오류");
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserId(String userId){
        return userRepository.findByUserId(userId);
    }


    public KioskSession setSession(User user){

        KioskSession session = null;

        if(user.isAdmin()){
            session = KioskSession.makeAdminSession(user.getUserId());
        }
        else{
            session = KioskSession.makeUserSession(user.getUserId());
        }

        session.login();

        return session;
    }

}
