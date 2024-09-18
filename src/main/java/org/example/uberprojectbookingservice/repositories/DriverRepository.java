package org.example.uberprojectbookingservice.repositories;

import org.example.uberprojectentityservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    @Modifying
    @Query("UPDATE Driver d SET d.isAvailable = :status WHERE d.id = :id ")
    void updateDriverAvailabilityById(@Param("id") Optional<Long> id, @Param("status") Boolean status);
}
