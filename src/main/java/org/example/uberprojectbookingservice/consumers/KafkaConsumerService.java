package org.example.uberprojectbookingservice.consumers;

import lombok.RequiredArgsConstructor;
import org.example.uberprojectbookingservice.dto.UpdateBookingRequestDto;
import org.example.uberprojectbookingservice.repositories.DriverRepository;
import org.example.uberprojectbookingservice.services.BookingServiceImpl;
import org.example.uberprojectbookingservice.services.RideServiceImpl;
import org.example.uberprojectentityservice.models.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final BookingServiceImpl bookingService;
    private final RideServiceImpl riderService;
    private  final DriverRepository driverRepository;
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);


    @KafkaListener(topics = "update-booking-topic", groupId = "booking_group")
    public void listen(UpdateBookingRequestDto updateBookingRequestDto) {
        bookingService.updateBooking(updateBookingRequestDto, updateBookingRequestDto.getBookingId());
        riderService.handleRideConfirmed(updateBookingRequestDto);
        Optional<Driver> driver = driverRepository.findById(updateBookingRequestDto.getDriverId().get());
        logger.info("Driver is available now? ----- {}",driver.get().isAvailable());
    }
}
