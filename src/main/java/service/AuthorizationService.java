package service;

import dao.AuthorizationDao;
import domain.UserInfo;

public class AuthorizationService {
    private final AuthorizationDao authDao;

    public AuthorizationService(AuthorizationDao authDao) {
        this.authDao = authDao;
    }

    public boolean authorizeUser(UserInfo userInfoInput) {
        if (userInfoInput.getRole() == null) {
            System.out.println("User role is null");
            return false;
        }
        if(userInfoInput.getUserId()<=0)
        {
            System.out.println("Invalid User ID");
            return false;
        }

        final UserInfo userInfoFromDb = authDao.authorizeUserById(userInfoInput.getUserId());
        if(userInfoFromDb==null) {
            System.out.println("DB returned null results");
            return false;
        }
        if (userInfoFromDb.isInActive()) {
            System.out.println("User is inactive");
            return false;
        }
        System.out.println("userInfoFromDb.getRole() = " + userInfoFromDb.getRole());
        System.out.println("userInfoInput.getRole() = " + userInfoInput.getRole());
        return userInfoFromDb.getRole().equals(userInfoInput.getRole());
    }
}
