package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.dto.HouseEdit;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface HouseService {

    List<HouseStatusDto> getCurrentMapStateForUser(User user);
    Map<Integer, HouseStatusDto> getHousesDtosForUserWithGroupAndLevel(User user, Group group, LevelSA level);

    User updateLevelGroupHousesTasksProgress(User user, Group group, LevelSA level);

    List<House> getAllHouses();
    House getHouseForGroupLevelMapId(Group group, LevelSA level, Integer mapId);
    House createHouse(int mapId, Group group, LevelSA level);
    House createHouse(int mapId, String name, Group group, LevelSA level);
    List<House> getHousesForGroupAndLevel(Group group, LevelSA level);
    void applyChanges(List<HouseEdit> edits);
}
