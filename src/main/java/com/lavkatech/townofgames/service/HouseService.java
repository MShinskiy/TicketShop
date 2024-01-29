package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseService {

    List<HouseStatusDto> getCurrentMapStateForUser(User user);
    List<HouseStatusDto> getHousesDtosForUser(User user);
    List<House> getAllHouses();
    House createHouse(int mapId, Group group, LevelSA level);
    House createHouse(int mapId, String name, Group group, LevelSA level);
    List<House> getHousesForGroupAndLevel(Group group, LevelSA level);

}
