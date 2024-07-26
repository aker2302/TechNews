package com.aker.TechNews.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class NewsLetterSub {

    @Id
    private String id;

    private String name;

    private String email;

    public NewsLetterSub(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
