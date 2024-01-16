package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.cosnt.Group;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseService {

    List<HouseStatusDto> getCurrentMapStateForUser(String dtprf);
    List<HouseStatusDto> getGroupHousesDtos(Group group);

}
