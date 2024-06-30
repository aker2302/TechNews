package com.aker.TechNews.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
@NoArgsConstructor
public class User {

    @Id
    String id;

    String name;

    
    @NonNull
    String email;

    String apiKey;
}
