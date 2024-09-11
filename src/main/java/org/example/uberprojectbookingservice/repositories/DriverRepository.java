package org.example.uberprojectbookingservice.repositories;

import org.example.uberprojectentityservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {
}
