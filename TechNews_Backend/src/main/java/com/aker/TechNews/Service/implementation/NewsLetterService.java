package com.aker.TechNews.Service.implementation;

import com.aker.TechNews.Repository.NewsLetterSubRepository;
import com.aker.TechNews.entity.NewsLetterSub;
import com.aker.TechNews.model.MailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsLetterService implements Runnable {

    @Autowired
    private NewsLetterSubRepository letterSubRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    private final String FRONT_END_URL = System.getenv("FRONT_END_URL");

    private static final long THREE_HOURS_IN_MILLISECONDS = 3 * 60 * 60 * 1000;

    @Override
    @Scheduled(initialDelay = THREE_HOURS_IN_MILLISECONDS ,fixedRate = 24 * 60 * 60 * 1000)
    public void run() {
        sendNewsLetter();
    }

    public void sendNewsLetter() {
        List<NewsLetterSub> newsLetterSubs = letterSubRepository.findAll();
        if (newsLetterSubs != null && !newsLetterSubs.isEmpty()) {
            for (NewsLetterSub newsLetterSub : newsLetterSubs) {
                Map<String, Object> model = new HashMap<>();
                model.put("Name", newsLetterSub.getName());
                model.put("Email", newsLetterSub.getEmail());
                model.put("URL", FRONT_END_URL);
                MailRequest request = new MailRequest();
                request.setDestination(newsLetterSub.getEmail());
                request.setName(newsLetterSub.getName());
                request.setSubject("Your Articles are ready");
                request.setFrom("technews.newsletter@gmail.com");

                emailSenderService.sendNewsLetterMail(request,model);
            }
        }
    }
}
