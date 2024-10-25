package com.asf.van.controller;

import com.asf.van.dao.UserDao;
import com.asf.van.model.BusinessHours;
import com.asf.van.model.Time;
import com.asf.van.model.User;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/user"})
public class LoginController {
    
    private static final Logger logger = Logger.getLogger(LoginController.class);
    
    @Autowired
    private UserDao userDao;

    /**
     * For user login
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    public User getUserDetails(@RequestParam String username, @RequestParam String password) {
        logger.info("/user/login invoked");
        User user = this.userDao.getUserDetails(username.toUpperCase(), password);
        logger.info("/user/login finished");
        return user;
    }

    /**
     * Updated on 17-02-2019 - Balaji Manickam - 4i Apps method to return
     * business hours - start time and end time
     *
     * @return
     */
    @GetMapping(value = "/login/time/business-hours")
    public BusinessHours getBusinessHours() {
        logger.info("/user/login/time/business-hours invoked");
        BusinessHours businessHours = this.userDao.getBusinessHours();
        logger.info("/user/login/time/business-hours finished");
        return businessHours;
    }

    /**
     * Updated on 19-02-2019 - Balaji Manickam - 4i Apps method to return
     * current log in time which is fetched from tomcat/application server
     *
     * @return
     */
    @GetMapping(value = "/login/time")
    public Time getCurrentLoginTime() {
        logger.info("/user/login/time invoked");
        Time time = this.userDao.getLoginTime(new Date());
        logger.info("/user/login/time finished");
        return time;
    }
}
