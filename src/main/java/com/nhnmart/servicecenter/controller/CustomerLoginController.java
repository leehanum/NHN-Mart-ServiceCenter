package com.nhnmart.servicecenter.controller;

import com.nhnmart.servicecenter.exception.LoginFailException;
import com.nhnmart.servicecenter.respository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/cs")
@Controller
public class CustomerLoginController {

    private UserRepository userRepository;

    @Autowired
    public CustomerLoginController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping("/login")
    public String login(){
        return "CustomerLoginForm";
    }


    @PostMapping("/login")
    public String doLogin(@RequestParam(name = "id") String id,
                          @RequestParam(name = "password") String password,
                          HttpServletRequest request,
                          HttpServletResponse response){

        if(userRepository.matches(id,password)){
            HttpSession session = request.getSession(true);
            session.setAttribute("studentId",id); // session(ID="abcde1234kljfasdkf",{studentId="lee"}) -> 서버에 저장됨
            session.setAttribute("아무거나","넣어보자");
            Cookie cookie = new Cookie("MYSESSION",session.getId()); // cookie("MYSESSION","abcde1234kljfasdkf") -> 브라우저에 저장
            response.addCookie(cookie);

            return "redirect:/cs/inquiry";
        }
        throw new LoginFailException();
//        return "redirect:/cs/login";
    }
}
