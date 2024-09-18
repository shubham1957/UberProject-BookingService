package org.example.uberprojectbookingservice.controllers;

import org.example.uberprojectbookingservice.dto.CreateBookingDto;
import org.example.uberprojectbookingservice.dto.CreateBookingResponseDto;
import org.example.uberprojectbookingservice.dto.UpdateBookingRequestDto;
import org.example.uberprojectbookingservice.dto.UpdateBookingResponseDto;
import org.example.uberprojectbookingservice.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    @PostMapping
    public ResponseEntity<CreateBookingResponseDto> createBooking(@RequestBody CreateBookingDto createBookingDto){

        return new ResponseEntity<>(bookingService.createBooking(createBookingDto), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}")
    public ResponseEntity<UpdateBookingResponseDto> updateBooking(@RequestBody UpdateBookingRequestDto requestDto, @PathVariable Long bookingId){
        return new ResponseEntity<>(bookingService.updateBooking(requestDto, bookingId), HttpStatus.OK);
    }
}
