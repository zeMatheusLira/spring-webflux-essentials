package com.devdojo.springwebfluxessentials.exception;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {

    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAtributesMap = super.getErrorAttributes((ServerRequest) webRequest, options);

        Throwable throwable = getError((ServerRequest) webRequest);
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException webClientResponseException = (WebClientResponseException) throwable;
            errorAtributesMap.put("message", webClientResponseException.getMessage());
            errorAtributesMap.put("developersMessage", "A ResponseStatusException Happened");
        }

        return errorAtributesMap;
    }
}
