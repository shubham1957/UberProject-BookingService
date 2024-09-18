package org.example.uberprojectbookingservice.consumers;

import org.example.uberprojectbookingservice.dto.UpdateBookingRequestDto;
import org.example.uberprojectbookingservice.services.BookingServiceImpl;
import org.example.uberprojectbookingservice.services.RideServiceImpl;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final BookingServiceImpl bookingService;
    private final RideServiceImpl riderService;

    public KafkaConsumerService(BookingServiceImpl bookingService, RideServiceImpl riderService) {
        this.bookingService = bookingService;
        this.riderService=riderService;
    }

    @KafkaListener(topics = "update-booking-topic", groupId = "booking_group")
    public void listen(UpdateBookingRequestDto updateBookingRequestDto) {
        bookingService.updateBooking(updateBookingRequestDto, updateBookingRequestDto.getBookingId());
        riderService.handleRideConfirmed(updateBookingRequestDto);
    }
}
