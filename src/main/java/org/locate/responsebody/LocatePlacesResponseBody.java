package org.locate.responsebody;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LocatePlacesResponseBody {

    private String result;
    private Boolean status;
    private String server_code;

}
