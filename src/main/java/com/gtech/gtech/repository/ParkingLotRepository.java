package com.gtech.gtech.repository;

import com.gtech.gtech.domain.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {

    List<ParkingLot> findByLotIdStartingWith(String prefix);
}
