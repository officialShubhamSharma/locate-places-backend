package org.locate.requestbody;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LocatePlacesRequestBody {

    private String currentCity;
    private String radiusInKm;
    private String noOfMembers;
    private String noOfDaysTrip;

}
