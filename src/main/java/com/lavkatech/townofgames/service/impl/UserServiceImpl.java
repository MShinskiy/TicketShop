package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.BalanceLog;
import com.lavkatech.townofgames.entity.HouseVisitLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.report.ImportDto;
import com.lavkatech.townofgames.repository.UserRepository;
import com.lavkatech.townofgames.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User getUserOrNull(String dtprf) {
        return userRepo.findUserByDtprf(dtprf).orElse(null);
    }

    @Override
    public void updateUsers(List<ImportDto> importLines) {
        for (ImportDto line : importLines) {
            //Get or create
            User user = userRepo.findUserByDtprf(line.dtprf()).orElse(new User(line.dtprf()));
            //Update
            user.setUserGroup(line.group());
            user.setCoins(user.getCoins() + line.coins());
            user.setPoints(user.getPoints() + line.points());
            /* TODO what to do with the tasks? */
        }
    }

    @Override
    public User createUser() {
        /*TODO add create user method*/
        return null;
    }

    @Override
    public void addVisitLog(User user, HouseVisitLog log) {
        user.getHouseVisitLog().add(log);
        userRepo.save(user);
    }

    @Override
    public void addBalanceLog(User user, BalanceLog log) {
        user.getBalanceLog().add(log);
        userRepo.save(user);
    }
}
