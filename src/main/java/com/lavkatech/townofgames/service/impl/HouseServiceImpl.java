package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.cosnt.Group;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.exception.UserNotFoundException;
import com.lavkatech.townofgames.repository.HouseRepository;
import com.lavkatech.townofgames.repository.UserRepository;
import com.lavkatech.townofgames.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepo;
    private final UserRepository userRepo;

    public HouseServiceImpl(HouseRepository houseRepo, UserRepository userRepo) {
        this.houseRepo = houseRepo;
        this.userRepo = userRepo;
    }


    @Override
    public List<HouseStatusDto> getCurrentMapStateForUser(String dtprf) throws UserNotFoundException {
        User user = userRepo.findUserByDtprf(dtprf).orElse(null);
        if(user == null)
            throw new UserNotFoundException(String.format("User with dtprf %s is not found", dtprf));



        return null;
    }

    @Override
    public List<HouseStatusDto> getGroupHousesDtos(Group group) {
        if(group.equals(Group.TEN)) {
            /* TODO Add groups to return specific
                maps state for each group */
        } else if (group.equals(Group.SEVEN)) {

        }

        List<House> allHouses = houseRepo.findAll();


        return null;
    }

    private HouseStatusDto houseToDto(House house) {

    }
}
