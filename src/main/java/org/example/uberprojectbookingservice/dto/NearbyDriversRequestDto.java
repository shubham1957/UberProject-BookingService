package org.example.uberprojectbookingservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyDriversRequestDto {
    String driverId;
    Double latitude;
    Double longitude;
}
