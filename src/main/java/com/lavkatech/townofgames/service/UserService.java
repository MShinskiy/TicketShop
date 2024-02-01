package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.BalanceLog;
import com.lavkatech.townofgames.entity.HouseVisitLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.report.ImportDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User getOrNull(String dtprf);

    User createUser(String dtprf);

    /*User getOrNull(String dtprf, Group group, LevelSA level);
    User getUserOrNull(String dtprf);*/
    void updateUsers(List<? extends ImportDto> importLines);
    User createUser(String dtprf, String username, Group group, LevelSA level);
    void addVisitLog(User user, HouseVisitLog log);
    void addBalanceLog(User user, BalanceLog log);
    User saveUserChanges(User user);
    User updateUserGroupLevel(User user, Group group, LevelSA level);

    User updateUserProgressTasks(User user);
}
