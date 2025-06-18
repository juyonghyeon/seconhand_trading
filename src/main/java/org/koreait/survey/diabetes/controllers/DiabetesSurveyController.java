package org.koreait.survey.diabetes.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/survey/diabetes")
@SessionAttributes("requestDiabetesSurvey")
public class DiabetesSurveyController {

    private final Utils utils;

    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("survey/diabetes/style");
    }

    @ModelAttribute("requestDiabetesSurvey")
    public RequestDiabetesSurvey requestDiabetesSurvey() {
        return new RequestDiabetesSurvey();
    }

    @GetMapping("/step1")
    public String step1(@ModelAttribute RequestDiabetesSurvey form) {

        return utils.tpl("survey/diabetes/step1");
    }



    @PostMapping("/step2")
    public String step2(@Valid RequestDiabetesSurvey form, Errors errors) {
        if (errors.hasErrors()) {
            return utils.tpl("survey/diabetes/step1");
        }

        return utils.tpl("survey/diabetes/step2");
    }


    /**
     * 설문 저장 및 결과 처리
     *
     * @param form
     * @param errors
     * @return
     */
    @PostMapping("/process")
    public String process(@Valid RequestDiabetesSurvey form, Errors errors) {

        if (errors.hasErrors()) {
            return utils.tpl("survey/diabetes/step2");
        }
        return "redirect:/survey/diabetes/result/설문번호";
    }


    /**
     * 설문 결과 보기
     */
    @GetMapping("/result/{seq}")
    public String result(@PathVariable("seq") Long seq) {


        return utils.tpl("survey/diabetes/result");
    }
}
