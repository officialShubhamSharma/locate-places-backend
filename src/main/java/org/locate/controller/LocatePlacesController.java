package org.locate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.locate.requestbody.LocatePlacesRequestBody;
import org.locate.service.LocatePlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/locate")
public class LocatePlacesController {
    @Autowired
    public LocatePlacesService locatePlacesService;

    @PostMapping("/best-places-for-me")
    public List<?> getPlacesForMe(@RequestBody LocatePlacesRequestBody locatePlacesBody) throws IOException {
        String locatePlacesServiceResponse = locatePlacesService.getPlaces(locatePlacesBody);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(locatePlacesServiceResponse, List.class);
    }

}
