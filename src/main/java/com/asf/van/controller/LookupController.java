package com.asf.van.controller;

import com.asf.van.dao.VanDao;
import com.asf.van.model.PopUp;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/lookup"})
public class LookupController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    @Autowired
    private VanDao vanDao;
    
    @PostMapping(value = "/message-text")
    public List<PopUp> getMessageText(@RequestParam String messageName) {
        LOGGER.info("/lookup/message-text invoked");
        List<PopUp> messages = this.vanDao.getMessageText(messageName);
        LOGGER.info("/lookup/message-text finished");
        return messages;
    }
}
