package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.BalanceLog;
import com.lavkatech.townofgames.entity.HouseVisitLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.repository.UserRepository;
import com.lavkatech.townofgames.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User getUser(String dtprf) {
        return userRepo.findUserByDtprf(dtprf).orElse(null);
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
