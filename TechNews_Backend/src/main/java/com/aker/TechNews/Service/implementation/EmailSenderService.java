package com.aker.TechNews.Service.implementation;

import com.aker.TechNews.model.MailRequest;
import com.aker.TechNews.response.MailResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfiguration;

    public MailResponse sendSubMail(MailRequest request, Map<String, Object> model) {
        MailResponse mailResponse = new MailResponse();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.addAttachment("templates/logo.png", new ClassPathResource("templates/logo.png"));

            Template temp = freemarkerConfiguration.getTemplate("Sub-mail-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(temp, model);

            helper.setTo(request.getDestination());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mailSender.send(mimeMessage);

            mailResponse.setMessage("Mail send to : " + request.getDestination());
            mailResponse.setStatus(Boolean.TRUE);
        } catch(MessagingException | IOException | TemplateException e){
            mailResponse.setMessage("Mail Sending failure : " + e.getMessage());
            mailResponse.setStatus(Boolean.FALSE);
        }

        return mailResponse;
    }


    public MailResponse sendNewsLetterMail(MailRequest request, Map<String, Object> model) {
        MailResponse mailResponse = new MailResponse();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            helper.addAttachment("templates/logo.png", new ClassPathResource("templates/logo.png"));

            Template temp = freemarkerConfiguration.getTemplate("NewsLetter-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(temp, model);

            helper.setTo(request.getDestination());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mailSender.send(mimeMessage);

            mailResponse.setMessage("mail send to : " + request.getDestination());
            mailResponse.setStatus(Boolean.TRUE);
        } catch(MessagingException | IOException | TemplateException e){
            mailResponse.setMessage("Mail Sending failure : " + e.getMessage());
            mailResponse.setStatus(Boolean.FALSE);
        }

        return mailResponse;
    }


}
