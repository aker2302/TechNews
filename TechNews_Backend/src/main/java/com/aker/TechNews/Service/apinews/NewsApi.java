package com.aker.TechNews.Service.apinews;

import com.aker.TechNews.model.ResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class NewsApi {

    private final WebClient webClient;
    private final String apikey = System.getenv("API_KEY");
    private final String newsUrl = "https://newsdata.io/api/1";


    public NewsApi(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(newsUrl).build();
    }

    public List<ResponseModel> getArticles(Long nextPage){
        if(nextPage != null){
            return webClient.get()
                    .uri("/news?language=en&category=technology&page="+ nextPage + "&apikey=" + apikey)
                    .retrieve()
                    .bodyToFlux(ResponseModel.class)
                    .collectList()
                    .block();
        }
       return webClient.get()
                .uri("/news?language=en&category=technology&apikey=" + apikey)
                .retrieve()
                .bodyToFlux(ResponseModel.class)
               .collectList()
               .block();
    }
}
