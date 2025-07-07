package com.nastyair.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {
    List<Person> persons = new ArrayList<>();

    @Tool(name = "getPerson", description = "Get a person by name")
    public String getPerson(String name) {
        //返回json格式
        ObjectMapper objectMapper = new ObjectMapper();
        String v = persons.stream()
                .filter(person -> person.name().equals(name))
                .findFirst()
                .map(person -> {
                    try {
                        return objectMapper.writeValueAsString(person);
                    } catch (JsonProcessingException e) {
                        return "{\"error\":\"JSON serialization failed\"}";
                    }
                })
                .orElse("{\"error\":\"Person not found\"}");

        return v;
    }

    @Tool(name = "sentEmail", description = "send email to a person")
    public String sendEmail(String name, String email, String subject, String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        //将邮件写入本地文件
        String emailFilePath = "email_" + name+"_"+System.currentTimeMillis()+ ".txt";
         try {
             java.io.FileWriter fileWriter = new java.io.FileWriter(emailFilePath);
             fileWriter.write(body);
             fileWriter.close();
         } catch (java.io.IOException e) {
             return "{\"error\":\"Failed to write email to file\"}";
         }
        try {
            return objectMapper.writeValueAsString(Map.of(
                "recipient", name,
                "email", email,
                "subject", subject,
                "body", body,
                "status", "sent"
            ));
        } catch (JsonProcessingException e) {
            return "{\"error\":\"JSON serialization failed\"}";
        }
    }
    @PostConstruct
    public void init() {
        persons.add(new Person("John", "1822222222", "john@example.com", "Apple", "CEO"));
        persons.add(new Person("Mary", "1833333333", "mary@example.com", "Google", "CTO"));
        persons.add(new Person("Bob", "1844444444", "bob@example.com", "Microsoft", "CFO"));
    }
}
