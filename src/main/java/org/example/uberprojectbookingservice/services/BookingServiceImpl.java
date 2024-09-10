package org.example.uberprojectbookingservice.services;
import org.example.uberprojectbookingservice.apis.LocationServiceApi;
import org.example.uberprojectbookingservice.dto.CreateBookingDto;
import org.example.uberprojectbookingservice.dto.CreateBookingResponseDto;
import org.example.uberprojectbookingservice.dto.DriverLocationDto;
import org.example.uberprojectbookingservice.dto.NearbyDriversRequestDto;
import org.example.uberprojectbookingservice.repositories.BookingRepository;
import org.example.uberprojectbookingservice.repositories.PassengerRepository;
import org.example.uberprojectentityservice.models.Booking;
import org.example.uberprojectentityservice.models.BookingStatus;
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

    //private static final String LOCATION_SERVICE = "http://localhost:7476";
    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository, LocationServiceApi locationServiceApi){
        this.passengerRepository=passengerRepository;
        this.bookingRepository = bookingRepository;
        this.locationServiceApi=locationServiceApi;
        this.restTemplate=new RestTemplate();
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

    private void processNearbyDriverAsync(NearbyDriversRequestDto requestDto){
        Call<DriverLocationDto[]> call = locationServiceApi.getNearbyDrivers(requestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                System.out.println(response.isSuccessful() +"---"+response.body());
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
