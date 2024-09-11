package org.example.uberprojectbookingservice.services;

import org.example.uberprojectbookingservice.dto.CreateBookingDto;
import org.example.uberprojectbookingservice.dto.CreateBookingResponseDto;
import org.example.uberprojectbookingservice.dto.UpdateBookingRequestDto;
import org.example.uberprojectbookingservice.dto.UpdateBookingResponseDto;

public interface BookingService {

    CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);

    UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId);
}
