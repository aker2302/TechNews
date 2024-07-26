package com.aker.TechNews.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class MailSenderConfig {

    @Bean
    public FreeMarkerConfigurationFactoryBean freemarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean = new FreeMarkerConfigurationFactoryBean();
        freeMarkerConfigurationFactoryBean.setTemplateLoaderPath("classpath:/templates");
        return freeMarkerConfigurationFactoryBean;
    }

}
