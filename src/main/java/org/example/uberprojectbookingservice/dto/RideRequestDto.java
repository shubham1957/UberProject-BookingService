package org.example.uberprojectbookingservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.ExactLocation;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {

    private Long passengerId;
    private ExactLocation startLocation;
    private ExactLocation endLocation;
    private Long bookingId;
    private List<Long> driverIds;
}
