package org.example.chatbot.api.domain.chatgpt;

import java.io.IOException;

/**
 *
 */

public interface IOpenAI {

    String doChatgpt(String question) throws IOException;

}
