package com.lavkatech.townofgames.repository;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HouseRepository extends JpaRepository<House, UUID> {

    List<House> findHouseByHouseGroupAndHouseLevel(Group group, LevelSA level);
    Optional<House> findHouseByHouseGroupAndHouseLevelAndMapId(Group group, LevelSA level, int mapId);
}
