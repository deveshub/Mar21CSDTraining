package service;

import dao.AuthorizationDao;
import domain.UserInfo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AuthorizationServiceTest {

    private AuthorizationDao authDao= Mockito.mock(AuthorizationDao.class);
    private AuthorizationService authService = new AuthorizationService(authDao);

    @After
    public void tearDown() throws Exception {

    }

    private UserInfo createUsersInput(long userId, String role)
    {
        UserInfo userInfoInput = new UserInfo();
        userInfoInput.setUserId(userId);
        userInfoInput.setRole(role);
        return userInfoInput;
    }

    private UserInfo createUsersDB(long userId, String userName, String role)
    {
        UserInfo userInfoInput = new UserInfo();
        userInfoInput.setUserId(userId);
        userInfoInput.setUserName(userName);
        userInfoInput.setRole(role);
        return userInfoInput;
    }

    @Test
    public void testAuthorizeUser_succeeds() {
        boolean expected = true;
        boolean actual;

        final int userId = 333;

        UserInfo userInfoFromDb = createUsersDB(userId, "CSD", "Admin");

        Mockito.when(authDao.authorizeUserById(userId)).thenReturn(userInfoFromDb);

        actual = authService.authorizeUser(createUsersInput(333,"Admin"));

        Mockito.verify(authDao).authorizeUserById(userId);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAuthorizeUser_fails_whenRoleHasDifferentCase() {
        boolean expected = false;
        boolean actual;

        final int userId = 333;
        UserInfo userInfoFromDb = createUsersDB(userId, "CSD", "Admin");

        Mockito.when(authDao.authorizeUserById(userId)).thenReturn(userInfoFromDb);

        actual = authService.authorizeUser(createUsersInput(userId,"admin"));
        // Mockito.verify(authDao, Mockito.times(1)).authorizeUserById(userId);
        Assert.assertEquals(expected, actual);

        // scenario 2 -- when role is different
        actual = authService.authorizeUser(createUsersInput(userId,"Manager"));
        Mockito.verify(authDao, Mockito.times(2)).authorizeUserById(userId);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAuthorizeUser_fails_whenRoleIsNull() {
        boolean expected = false;
        boolean actual;

        final int userId = 333;

        actual = authService.authorizeUser(createUsersInput(userId,null));

        Mockito.verifyZeroInteractions(authDao);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAuthorizeUser_fails_whenUserIdIsNull() {
        boolean expected = false;
        boolean actual;

        actual = authService.authorizeUser(createUsersInput(000,"Admin"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAuthorizeUser_fails_whenUserIsIsNotPresentInDB() {

        boolean expected = false;
        boolean actual;

        Mockito.when(authDao.authorizeUserById(999)).thenReturn(null);
        actual = authService.authorizeUser(createUsersInput(999,"Admin"));

        Mockito.verify(authDao).authorizeUserById(999);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAuthorizeUser_shouldFail_whenUserIsInactive() {
        boolean expected = false;
        boolean actual;

        UserInfo inactiveUser = buildInactiveUser(999, "CSD", "Manager");
        Mockito.when(authDao.authorizeUserById(999)).thenReturn(inactiveUser);

        actual = authService.authorizeUser(createUsersInput(999, "Manager"));
        Mockito.verify(authDao).authorizeUserById(999);

        Assert.assertEquals(expected, actual);
    }

    private UserInfo buildInactiveUser(int userId, String userName, String role) {
        UserInfo user = new UserInfo();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setRole(role);
        user.setInActive(true);
        return user;
    }
}



