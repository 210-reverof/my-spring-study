package com.wy.chatting.domain.chatgpt.entity.question;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GPTQuestionRepository extends JpaRepository<GPTQuestion, Integer> {

}