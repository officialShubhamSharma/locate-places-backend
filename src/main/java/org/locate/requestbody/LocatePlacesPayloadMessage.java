package org.locate.requestbody;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LocatePlacesPayloadMessage {

    private String role;
    private String content;

}
