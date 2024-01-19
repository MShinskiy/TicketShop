package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseService {

    List<HouseStatusDto> getCurrentMapStateForUser(User user);
    List<HouseStatusDto> getHousesDtosForUser(User user);
    List<House> getAllHouses();

}
