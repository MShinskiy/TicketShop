package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.*;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.exception.UserNotFoundException;
import com.lavkatech.townofgames.repository.HouseRepository;
import com.lavkatech.townofgames.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepo;

    public HouseServiceImpl(HouseRepository houseRepo) {
        this.houseRepo = houseRepo;
    }


    @Override
    public List<HouseStatusDto> getCurrentMapStateForUser(User user) throws UserNotFoundException {

        return null;
    }

    @Override
    public List<HouseStatusDto> getHousesDtosForUser(User user) {
        final Group userGroup = user.getUserGroup();
        final LevelSA userLevel = user.getUserLevel();
        return houseRepo.findAll()
                .stream()
                // Отфильтровать дома соответственно группе
                .filter(house -> userGroup == house.getHouseGroup() && userLevel == house.getHouseLevel())
                // Запаковать в dto
                .map(house -> house.toDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<House> getAllHouses() {
        return houseRepo.findAll();
    }
}
