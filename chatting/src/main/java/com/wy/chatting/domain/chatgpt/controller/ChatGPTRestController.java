package com.wy.chatting.domain.chatgpt.controller;

import com.wy.chatting.domain.chatgpt.service.GPTChatRestService;
import com.wy.chatting.domain.chatgpt.service.dto.request.GPTCompletionChatRequest;
import com.wy.chatting.domain.chatgpt.service.dto.request.GPTCompletionRequest;
import com.wy.chatting.domain.chatgpt.service.dto.response.CompletionChatResponse;
import com.wy.chatting.domain.chatgpt.service.dto.response.CompletionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatgpt/rest")
@RequiredArgsConstructor
public class ChatGPTRestController {

    private final GPTChatRestService gptChatRestService;

    @PostMapping("/completion")
    public CompletionResponse completion(final @RequestBody GPTCompletionRequest gptCompletionRequest) {
        return gptChatRestService.completion(gptCompletionRequest);
    }

    @PostMapping("/completion/chat")
    public CompletionChatResponse completionChat(final @RequestBody GPTCompletionChatRequest gptCompletionChatRequest) {
        return gptChatRestService.completionChat(gptCompletionChatRequest);
    }

}