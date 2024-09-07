package org.example.uberprojectbookingservice.services;

import org.example.uberprojectbookingservice.dto.CreateBookingDto;
import org.example.uberprojectbookingservice.dto.CreateBookingResponseDto;
import org.example.uberprojectentityservice.models.Booking;

public interface BookingService {

    CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);
}
