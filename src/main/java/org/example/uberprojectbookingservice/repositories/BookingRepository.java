package org.example.uberprojectbookingservice.repositories;

import org.example.uberprojectentityservice.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
}
