package top.archiesean.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ceshi
 *
 * @Author ArchieSean
 * @Date 2022-07-07 21:21
 */
@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("demo")
    public String test() {
        return "123";
    }
}
