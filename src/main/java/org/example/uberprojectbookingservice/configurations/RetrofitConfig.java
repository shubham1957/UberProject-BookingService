package org.example.uberprojectbookingservice.configurations;

import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.example.uberprojectbookingservice.apis.LocationServiceApi;
import org.example.uberprojectbookingservice.apis.UberSocketApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    private final EurekaClient eurekaClient;

    public RetrofitConfig(EurekaClient eurekaClient){
        this.eurekaClient=eurekaClient;
    }

    private String getServiceUrl(String serviceName){
        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi locationServiceApi(){
        return  new Retrofit.Builder()
                .baseUrl(getServiceUrl("LOCATION-SERVICE"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);
    }

    @Bean
    public UberSocketApi uberSocketApi(){
        return  new Retrofit.Builder()
                .baseUrl(getServiceUrl("SOCKET-SERVER"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(UberSocketApi.class);
    }

}
