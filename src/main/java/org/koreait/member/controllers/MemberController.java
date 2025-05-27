package org.koreait.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.koreait.member.validators.JoinValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final Utils utils;
    private final JoinValidator joinValidator;

    // 회원가입 양식
    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin  form, Model model) {
        return utils.tpl("member/join");
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String JoinPs(@Valid RequestJoin form, Errors errors) {
        joinValidator.validate(form, errors);
        if (errors.hasErrors()) {
            return utils.tpl("member/join");
        }

        return "redirect:/member/login";

    }
}