package org.example.uberprojectbookingservice.services;
import jakarta.transaction.Transactional;
import org.example.uberprojectbookingservice.apis.LocationServiceApi;
import org.example.uberprojectbookingservice.dto.*;
import org.example.uberprojectbookingservice.repositories.BookingRepository;
import org.example.uberprojectbookingservice.repositories.DriverRepository;
import org.example.uberprojectbookingservice.repositories.PassengerRepository;
import org.example.uberprojectentityservice.models.Booking;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectentityservice.models.Driver;
import org.example.uberprojectentityservice.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final RestTemplate restTemplate;
    private final LocationServiceApi locationServiceApi;
    private final DriverRepository driverRepository;

    //private static final String LOCATION_SERVICE = "http://localhost:7476";
    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository, LocationServiceApi locationServiceApi, DriverRepository driverRepository){
        this.passengerRepository=passengerRepository;
        this.bookingRepository = bookingRepository;
        this.locationServiceApi=locationServiceApi;
        this.restTemplate=new RestTemplate();
        this.driverRepository = driverRepository;
    }

    @Override
    public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails) {

        Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(bookingDetails.getStartLocation())
                .endLocation(bookingDetails.getEndLocation())
                .passenger(passenger.get())
                .build();

        Booking newBooking = bookingRepository.save(booking);

        NearbyDriversRequestDto request = NearbyDriversRequestDto.builder()
                .latitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();

        processNearbyDriverAsync(request);
//
//        // Make a call to Location-Service to fetch nearby drivers
//        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE + "/api/location/nearby/drivers/1", request ,DriverLocationDto[].class);
//
//        if(result.getStatusCode().is2xxSuccessful() && result.getBody() != null){
//            List<DriverLocationDto> driverLocations = Arrays.asList(result.getBody());
//            driverLocations.forEach(driverLocationDto -> System.out.println("Driver Id : "+driverLocationDto.getDriverId()+" lat : "+driverLocationDto.getLatitude()+" long : "+driverLocationDto.getLongitude()));
//        }

        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }

    // update booking after any driver performs an action on booking request raised by passenger
    @Override
    @Transactional
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId) {
        Optional<Driver> driver = driverRepository.findById(bookingRequestDto.getDriverId().get());
        bookingRepository.updateBookingStatusAndDriverById(bookingId,BookingStatus.SCHEDULED,driver.get());
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return UpdateBookingResponseDto.builder()
                .bookingId(bookingId)
                .status(booking.get().getBookingStatus())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();
    }

    // Make an async call to the location Service to get Nearby drivers
    private void processNearbyDriverAsync(NearbyDriversRequestDto requestDto){
        Call<DriverLocationDto[]> call = locationServiceApi.getNearbyDrivers(requestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                     List<DriverLocationDto> driverLocations = Arrays.asList(response.body());
                     driverLocations.forEach(driverLocationDto -> System.out.println("Driver Id : "+driverLocationDto.getDriverId()+" lat : "+driverLocationDto.getLatitude()+" long : "+driverLocationDto.getLongitude()));
                }
                else {
                    System.out.println("Request Failed : "+response.message());
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
