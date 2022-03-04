package com.kakao.cafe.controller.member;

import com.kakao.cafe.domain.member.Member;
import com.kakao.cafe.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/users")
    public ModelAndView join(Member member, ModelAndView model) {

        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            model.getModel().put("member", member);
            model.getModel().put("errorMessage", e.getMessage());
            model.setStatus(HttpStatus.BAD_REQUEST);
            model.setViewName("user/form");
            return model;
        }

        model.setViewName("redirect:/users");
        return model;
    }

    @GetMapping("/users")
    public ModelAndView memberList(ModelAndView modelAndView) {
        memberService.findMembers()
                .ifPresent(members -> modelAndView.addObject("members", members));
        modelAndView.setViewName("user/list");
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView findProfile(@PathVariable("id") Long id, ModelAndView modelAndView) {

        memberService.findOne(id)
                .ifPresentOrElse(member -> {
                    modelAndView.setViewName("user/profile");
                    modelAndView.addObject(member);
                }, () -> {
                    modelAndView.setViewName("user/list");
                    modelAndView.setStatus(HttpStatus.BAD_REQUEST);
                    modelAndView.addObject("errorMessage", "회원이 없습니다.");
                });

        return modelAndView;
    }

}
