package org.example.uberprojectbookingservice.services;

import org.example.uberprojectbookingservice.dto.UpdateBookingRequestDto;
import org.example.uberprojectbookingservice.dto.UpdateRideStatusDto;

public interface RideService {
    void updateRideStatus(UpdateRideStatusDto rideStatusDto);
    void handleRideConfirmed(UpdateBookingRequestDto requestDto);
    void startRide(UpdateBookingRequestDto updateBookingRequestDto);
    void rideCompleted(UpdateBookingRequestDto updateRideRequestDto);
}
