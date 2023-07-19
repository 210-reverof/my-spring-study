package com.wy.chatting.domain.chatgpt.service;

import com.theokanning.openai.completion.CompletionResult;
import com.wy.chatting.domain.chatgpt.service.dto.response.CompletionResponse.Message;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import com.wy.chatting.domain.chatgpt.entity.answer.GPTAnswer;
import com.wy.chatting.domain.chatgpt.entity.answer.GPTAnswerRepository;
import com.wy.chatting.domain.chatgpt.entity.question.GPTQuestion;
import com.wy.chatting.domain.chatgpt.entity.question.GPTQuestionRepository;
import com.wy.chatting.domain.chatgpt.service.dto.request.GPTCompletionChatRequest;
import com.wy.chatting.domain.chatgpt.service.dto.request.GPTCompletionRequest;
import com.wy.chatting.domain.chatgpt.service.dto.response.CompletionChatResponse;
import com.wy.chatting.domain.chatgpt.service.dto.response.CompletionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPTChatRestService {
    private final OpenAiService openAiService;

    private final GPTQuestionRepository questionRepository;

    private final GPTAnswerRepository answerRepository;

    @Transactional
    public CompletionResponse completion(final GPTCompletionRequest restRequest) {
        CompletionResult result = openAiService.createCompletion(GPTCompletionRequest.of(restRequest));
        CompletionResponse response = CompletionResponse.of(result);

        List<String> messages = response.getMessages().stream()
                .map(Message::getText)
                .collect(Collectors.toList());

        GPTAnswer gptAnswer = saveAnswer(messages);
        saveQuestion(restRequest.getPrompt(), gptAnswer);

        return response;
    }

    @Transactional
    public CompletionChatResponse completionChat(GPTCompletionChatRequest gptCompletionChatRequest) {
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(
                GPTCompletionChatRequest.of(gptCompletionChatRequest));

        CompletionChatResponse response = CompletionChatResponse.of(chatCompletion);

        List<String> messages = response.getMessages().stream()
                .map(CompletionChatResponse.Message::getMessage)
                .collect(Collectors.toList());

        GPTAnswer gptAnswer = saveAnswer(messages);

        saveQuestion(gptCompletionChatRequest.getMessage(), gptAnswer);

        return response;
    }

    private void saveQuestion(String question, GPTAnswer answer) {
        GPTQuestion questionEntity = new GPTQuestion(question, answer);
        questionRepository.save(questionEntity);
    }

    private GPTAnswer saveAnswer(List<String> response) {

        String answer = response.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining());

        return answerRepository.save(new GPTAnswer(answer));
    }
}