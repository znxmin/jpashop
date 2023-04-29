package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class HomeController {


    @RequestMapping(value = "/")
    public String home() {
        log.debug("home controller");
        return "home";
    }
}
