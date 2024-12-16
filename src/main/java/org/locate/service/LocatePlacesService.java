package org.locate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locate.config.RestTemplateConfig;
import org.locate.requestbody.LocatePlacesRequestBody;
import org.locate.requestbody.LocatePlacesPayload;
import org.locate.requestbody.LocatePlacesPayloadMessage;
import org.locate.responsebody.LocatePlacesResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocatePlacesService {

    @Value("${chat.gpt.url}")
    private String chatGptUrl;
    @Value("${rapid.api.key}")
    private String rapidApiKey;
    @Value("${rapid.api.host}")
    private String rapidApiHost;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @Cacheable(value = "apiCache", key = "#root.methodName + ':' + #normalizedBody")
    public String getPlaces(LocatePlacesRequestBody locatePlacesBody) throws JsonProcessingException {
        String normalizedBody = normalizeJson(locatePlacesBody);
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key",rapidApiKey);
        headers.set("x-rapidapi-host",rapidApiHost);
        headers.set("Content-Type", "application/json");
        HttpEntity<Object> requestEntity=new HttpEntity<>(createLocatePlacesPayload(locatePlacesBody),headers);
        String result= restTemplateConfig.restTemplate().exchange(
                chatGptUrl,
                HttpMethod.POST,
                requestEntity,
                LocatePlacesResponseBody.class
        ).getBody().getResult();
        System.out.println("getPlaces() result : "+result);
        return result.substring(result.indexOf("["));
    }

    private LocatePlacesPayload createLocatePlacesPayload(LocatePlacesRequestBody locatePlacesBody) {
        List<LocatePlacesPayloadMessage> locatePlacesPayloadMessageList=new ArrayList<>();
        LocatePlacesPayloadMessage locatePlacesPayloadMessage=new LocatePlacesPayloadMessage();
        locatePlacesPayloadMessage.setRole("user");
        locatePlacesPayloadMessage.setContent("Answer of below question should be is json format for every place , " +
                "and don't include explanation , " +
                "just one word answers for every place will work fine. \n" +
                "based on current date, tell me top 5 best places with current date as best & safe(mostly whether safe) time to visit near " +
                locatePlacesBody.getCurrentCity()+" assuming I can travel "+locatePlacesBody.getRadiusInKm()+" km of radius. " +
                "along with the average cost of "+locatePlacesBody.getNoOfMembers()+" person if considered "+locatePlacesBody.getNoOfDaysTrip()+" days trip " +
                "along with a 5 liner best , simple and unique description on why the place is worth going"+
                "\n I again repeat I want the data in json format surrounded with '[' and ']', which should include the following json key : " +
                "Total_Distance,Place,Average_Cost,Description");
        locatePlacesPayloadMessageList.add(locatePlacesPayloadMessage);
        System.out.println(locatePlacesPayloadMessage.getContent());
        LocatePlacesPayload locatePlacesPayload=new LocatePlacesPayload();
        locatePlacesPayload.setMessages(locatePlacesPayloadMessageList);
        locatePlacesPayload.setWeb_access(false);
        return locatePlacesPayload;
    }

    public String normalizeJson(LocatePlacesRequestBody jsonMap) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(jsonMap).toLowerCase();
    }
}
