package org.example.uberprojectbookingservice.services;
import jakarta.transaction.Transactional;
import org.example.uberprojectbookingservice.apis.LocationServiceApi;
import org.example.uberprojectbookingservice.apis.UberSocketApi;
import org.example.uberprojectbookingservice.dto.*;
import org.example.uberprojectbookingservice.repositories.BookingRepository;
import org.example.uberprojectbookingservice.repositories.DriverRepository;
import org.example.uberprojectbookingservice.repositories.PassengerRepository;
import org.example.uberprojectentityservice.models.Booking;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectentityservice.models.Driver;
import org.example.uberprojectentityservice.models.Passenger;
import org.springframework.stereotype.Service;
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
    private final LocationServiceApi locationServiceApi;
    private final DriverRepository driverRepository;
    private final UberSocketApi uberSocketApi;

    public BookingServiceImpl(PassengerRepository passengerRepository,
                              BookingRepository bookingRepository,
                              LocationServiceApi locationServiceApi,
                              DriverRepository driverRepository,
                              UberSocketApi uberSocketApi){
        this.passengerRepository=passengerRepository;
        this.bookingRepository=bookingRepository;
        this.locationServiceApi=locationServiceApi;
        this.driverRepository=driverRepository;
        this.uberSocketApi=uberSocketApi;
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

        processNearbyDriverAsync(request, bookingDetails.getPassengerId(), newBooking.getId());

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
        // TODO: if(driver.isPresent() && driver.get().isAvailable()){ then only assign the driver }
        bookingRepository.updateBookingStatusAndDriverById(bookingId,BookingStatus.SCHEDULED,driver.get());
        // TODO : driverRepository.update -> after assigning the driver update it as unavailable

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return UpdateBookingResponseDto.builder()
                .bookingId(bookingId)
                .status(booking.get().getBookingStatus().toString())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();
    }

    // Make an async call to the location Service to get Nearby drivers
    private void processNearbyDriverAsync(NearbyDriversRequestDto requestDto, Long passengerId, Long bookingId){
        Call<DriverLocationDto[]> call = locationServiceApi.getNearbyDrivers(requestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                    System.out.println("The nearby drivers are : ");
                    List<DriverLocationDto> driverLocations = Arrays.asList(response.body());
                    driverLocations.forEach(driverLocationDto -> System.out.println("Driver Id : "+driverLocationDto.getDriverId()+" lat : "+driverLocationDto.getLatitude()+" long : "+driverLocationDto.getLongitude()));
                    try {
                        raiseRideRequestAsync(RideRequestDto.builder().passengerId(passengerId).bookingId(bookingId).build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    System.out.println("Request to process nearby drivers failed !! : "+response.message());
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void raiseRideRequestAsync(RideRequestDto requestDto){
        Call<Boolean> call = uberSocketApi.raiseRideRequest(requestDto);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null){
                    Boolean result = response.body().booleanValue();
                    System.out.println("Driver response is : " + result);

                }
                else{
                    System.out.println("Request for new ride failed !!"+response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
