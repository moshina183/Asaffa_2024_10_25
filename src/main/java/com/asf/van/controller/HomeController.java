package com.asf.van.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final Logger logger = Logger.getLogger(HomeController.class);

    @GetMapping(value = "/h123")
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}." + locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(1, 1, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        return "login";
    }

    @GetMapping(value = "/home")
    public String details(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}." + locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(1, 1, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        return "login";
    }
}
