package org.example.uberprojectbookingservice.services;

import jakarta.transaction.Transactional;
import org.example.uberprojectbookingservice.dto.UpdateBookingRequestDto;
import org.example.uberprojectbookingservice.dto.UpdateRideStatusDto;
import org.example.uberprojectbookingservice.repositories.BookingRepository;
import org.example.uberprojectbookingservice.repositories.DriverRepository;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectentityservice.models.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RideServiceImpl implements RideService{

    private final Logger logger = LoggerFactory.getLogger(RideServiceImpl.class);
    private final BookingRepository bookingRepository;
    private  final DriverRepository driverRepository;

    public RideServiceImpl(BookingRepository bookingRepository, DriverRepository driverRepository){
        this.bookingRepository=bookingRepository;
        this.driverRepository=driverRepository;
    }

    @Override
    public void updateRideStatus(UpdateRideStatusDto rideStatusDto) {
        if (rideStatusDto.getBookingId() != null) {
            try {
                bookingRepository.updateBookingStatusById(rideStatusDto.getBookingId(), rideStatusDto.getStatus());
            }
            catch (Exception e) {
                logger.error("Failed to update ride status for booking ID: {}", rideStatusDto.getBookingId(), e);
            }
        } else {
            logger.warn("Booking ID is null. Cannot update ride status.");
        }
    }

    @Override
    @Transactional
    public void handleRideConfirmed(UpdateBookingRequestDto updateBookingRequestDto) {
        logger.info("RIDE CONFIRMED, DRIVER IS JUST 3 MIN AWAY");
        driverRepository.updateDriverAvailabilityById(updateBookingRequestDto.getDriverId(), false);
        Optional<Driver> driver = driverRepository.findById(updateBookingRequestDto.getDriverId().get());
        logger.info("Booking Id: {}, Driver Id: {} , Status: {}, Driver status : {}",
                updateBookingRequestDto.getBookingId(), updateBookingRequestDto.getDriverId(), updateBookingRequestDto.getStatus(), driver.get().isAvailable());
        startRide(updateBookingRequestDto);
    }

    @Override
    public void startRide(UpdateBookingRequestDto updateBookingRequestDto) {
        logger.info("***RIDE STARTED***");
        UpdateRideStatusDto rideStatusDto = UpdateRideStatusDto.builder()
                .bookingId(updateBookingRequestDto.getBookingId())
                .status(BookingStatus.IN_RIDE)
                .build();

        logger.info("Status After Ride started : Status: {}",rideStatusDto.getStatus());

        updateRideStatus(rideStatusDto);
        rideCompleted(updateBookingRequestDto);
    }

    @Override
    public void rideCompleted(UpdateBookingRequestDto bookingRequestDto){
        logger.info("Ride has completed");
        UpdateRideStatusDto rideStatusDto = UpdateRideStatusDto.builder()
                .bookingId(bookingRequestDto.getBookingId())
                .status(BookingStatus.COMPLETED)
                .build();

        logger.info("Status After Ride completed : Status: {}, Driver Id : {} ",rideStatusDto.getStatus(), bookingRequestDto.getDriverId());
        updateRideStatus(rideStatusDto);
        //make driver available
        driverRepository.updateDriverAvailabilityById(bookingRequestDto.getDriverId(), true);
        // make an async call to review service to enter a review
    }
}
