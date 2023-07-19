package com.wy.chatting.global.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ChatGPTConfig {
    private String token = "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";

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(token, Duration.ofSeconds(60));
    }



}
