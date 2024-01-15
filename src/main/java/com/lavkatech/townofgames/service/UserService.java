package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.BalanceLog;
import com.lavkatech.townofgames.entity.HouseVisitLog;
import com.lavkatech.townofgames.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getUser(String dtprf);
    User createUser();
    void addVisitLog(User user, HouseVisitLog log);
    void addBalanceLog(User user, BalanceLog log);
}
