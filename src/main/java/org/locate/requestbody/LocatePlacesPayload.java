package org.locate.requestbody;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.util.List;

@Getter
@Setter
@Component
public class LocatePlacesPayload {

    private List<LocatePlacesPayloadMessage> messages;
    private Boolean web_access;

}
