package org.example.uberprojectbookingservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.Driver;

import javax.swing.text.html.Option;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingResponseDto {
    private Long bookingId;
    private String bookingStatus;
    private Optional<Driver> driver;
}
