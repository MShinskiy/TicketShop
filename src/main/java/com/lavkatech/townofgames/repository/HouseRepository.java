package com.lavkatech.townofgames.repository;

import com.lavkatech.townofgames.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, UUID> {

}
