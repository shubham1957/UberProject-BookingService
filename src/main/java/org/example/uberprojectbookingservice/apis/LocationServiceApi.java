package org.example.uberprojectbookingservice.apis;

import org.example.uberprojectbookingservice.dto.DriverLocationDto;
import org.example.uberprojectbookingservice.dto.NearbyDriversRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {

    @POST("/api/location/nearby/drivers/100")
    Call<DriverLocationDto[]> getNearbyDrivers(@Body NearbyDriversRequestDto requestDto);
}
