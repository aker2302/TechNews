package com.aker.TechNews.model;

import lombok.Data;

@Data
public class MailRequest {

    private String name;
    private String destination;
    private String subject;
    private String from;

}
