package org.koreait.test.controllers;

import org.koreait.global.exceptions.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test2")
public class Test2Controller {

    @GetMapping
    public void test1() {
        boolean result = false;
        if (!result) {
            throw new BadRequestException("REST 예외 발생!");
        }
    }
}