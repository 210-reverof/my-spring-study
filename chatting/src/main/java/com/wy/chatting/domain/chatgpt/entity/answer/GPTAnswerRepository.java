package com.wy.chatting.domain.chatgpt.entity.answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GPTAnswerRepository extends JpaRepository<GPTAnswer, Integer> {
}