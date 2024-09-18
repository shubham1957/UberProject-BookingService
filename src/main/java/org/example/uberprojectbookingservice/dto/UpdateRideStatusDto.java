package org.example.uberprojectbookingservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.BookingStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRideStatusDto {

    private Long bookingId;
    private BookingStatus status;
}
