package org.example.uberprojectbookingservice.repositories;

import org.example.uberprojectentityservice.models.Booking;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectentityservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = :status, b.driver = :driver WHERE b.id = :id")
    void updateBookingStatusAndDriverById(@Param("id") Long id, @Param("status") BookingStatus status, @Param("driver") Driver driver);

    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = :status WHERE b.id = :id")
    void updateBookingStatusById(@Param("id") Long id, @Param("status") BookingStatus status);

}
