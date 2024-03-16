package com.aker.TechNews.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Article {
    @Id
    String id;
    String title;
    String link;
    List<String> keywords;
    String description;
    String pubDate;
    String image_url;
    String source_id;
    String source_url;
    String source_icon;
    String source_priority;
    List<String> country;
    List<String> category;
    String language;
}
