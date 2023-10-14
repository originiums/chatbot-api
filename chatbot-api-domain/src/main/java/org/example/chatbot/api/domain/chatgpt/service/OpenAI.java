package org.example.chatbot.api.domain.chatgpt.service;

import com.alibaba.fastjson.JSON;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.chatbot.api.domain.chatgpt.IOpenAI;
import org.example.chatbot.api.domain.chatgpt.model.aggregates.Aians;
import org.example.chatbot.api.domain.chatgpt.model.vo.Choices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OpenAI implements IOpenAI {

    private Logger logger = LoggerFactory.getLogger(OpenAI.class);

    @Value("${chatbot-api.curl}")
    private String crul;

    @Value("${chatbot-api.chatGPT-Key}")
    private String GPTKey;

    @Override
    public String doChatgpt(String question) throws IOException {
        String pro = "127.0.0.1";//本机地址
        int pro1 = 15732; //代理端口号
        //创建一个 HttpHost 实例，这样就设置了代理服务器的主机和端口。
        HttpHost httpHost = new HttpHost(pro, pro1);
        //创建一个 RequestConfig 对象，然后使用 setProxy() 方法将代理 httpHost 设置进去。
        RequestConfig build = RequestConfig.custom().setProxy(httpHost).build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(crul);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + GPTKey);
        post.setConfig(build);

        String paramJson = "{\n" +
                "     \"model\": \"gpt-3.5-turbo\",\n" +
                "     \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}],\n" +
                "     \"temperature\": 0.7\n" +
                "   }";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonstr = EntityUtils.toString(response.getEntity());
            //System.out.println(res);
            Aians aians = JSON.parseObject(jsonstr, Aians.class);
            StringBuilder stringBuilder = new StringBuilder();
            List<Choices> choices = aians.getChoices();
            for (Choices choices1 : choices){
                stringBuilder.append(choices1.getMessage().getContent());
            }
            //System.out.println(stringBuilder.toString());
            return stringBuilder.toString();
        } else {
            throw new RuntimeException("api.openai.com Err Code is " + response.getStatusLine().getStatusCode());
        }
    }

}
