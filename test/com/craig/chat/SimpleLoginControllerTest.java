package com.craig.chat;

import com.craig.chat.LoginController;
import static junit.framework.Assert.*;
import com.craig.chat.SimpleLoginController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: clcraig
 * Date: 2/19/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(RobolectricTestRunner.class)
public class SimpleLoginControllerTest {

    private SimpleLoginController simpleLoginController;

    @Before
    public void setUp() throws Exception {
        simpleLoginController = new SimpleLoginController();
    }

    @Test
    public void simpleLoginControllerImplementsLoginController() throws Exception {
        assertTrue(simpleLoginController instanceof LoginController);
    }

    @Test
    public void onlySelectUserNameAndPasswordsAreValid() throws Exception {
        List<String[]>validCredentials = new ArrayList<String[]>(3);
        validCredentials.add(new String[]{"cliff", "craig"});
        validCredentials.add(new String[]{"onur", "cinar"});
        validCredentials.add(new String[]{"nizam", "gok"});
        for(String[] eachCredential : validCredentials) {
            assertTrue("eachCredential " + eachCredential[0] + ", " + eachCredential[1] + " should be valid.",
                    simpleLoginController.login(eachCredential[0], eachCredential[1]));
        }
    }
}
