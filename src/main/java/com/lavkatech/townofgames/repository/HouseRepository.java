package com.lavkatech.townofgames.repository;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.cosnt.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, UUID> {

    List<House> findAllByHouseGroup(Group group);
}
