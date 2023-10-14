package org.example.chatbot.api.application.req;


import org.example.chatbot.api.domain.chatgpt.IOpenAI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("chat/bot")
public class GetAns {

    @Resource
    private IOpenAI openAI;

    @RequestMapping("ans")
    public String queryAnsWithQ(String question) throws IOException {
        String s = openAI.doChatgpt(question);
        System.out.println(s);
        return s;
    }
}
