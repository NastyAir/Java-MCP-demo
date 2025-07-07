package com.nastyair.ai;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import java.util.concurrent.CompletableFuture;

public class TestLC {
    private static final String OLLAMA_BASE_URL = "http://localhost:11434";
    private static final String OLLAMA_MODEL_NAME = "qwen3:1.7b";
    public static void main(String[] args) {
        OllamaStreamingChatModel model = OllamaStreamingChatModel.builder()
                .baseUrl(OLLAMA_BASE_URL)
                .modelName(OLLAMA_MODEL_NAME)
                .temperature(0.0)
                .build();
        String userMessage = "Write a 100-word poem about Java and AI";

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();
        model.chat(userMessage, new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.print(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                futureResponse.complete(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });

        futureResponse.join();
    }
}

