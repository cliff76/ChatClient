package com.craig.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: clcraig
 * Date: 2/19/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleLoginController implements LoginController{
    List<String[]> validCredentials = new ArrayList<String[]>(3);

    public SimpleLoginController() {
        validCredentials.add(new String[]{"cliff", "craig"});
        validCredentials.add(new String[]{"onur", "cinar"});
        validCredentials.add(new String[]{"nizam", "gok"});
    }

    @Override
    public boolean login(String userName, String password) {
        for (String[] eachCredential : validCredentials) {
            if(eachCredential[0].equals(userName) && eachCredential[1].equals(password)) return true;
        }
        return false;
    }
}
