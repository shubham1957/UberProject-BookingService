package org.example.uberprojectbookingservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.BookingStatus;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequestDto {

    private BookingStatus status;
    private Optional<Long> driverId;

}
