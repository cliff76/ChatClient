package com.craig.chat;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.craig.chat.ChatActivity;
import com.craig.chat.LaunchActivity;
import com.craig.chat.LoginController;
import org.jmock.MockObjectTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import static org.robolectric.Robolectric.shadowOf;

import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: clcraig
 * Date: 2/19/13
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(RobolectricTestRunner.class)
public class LoginTest extends MockObjectTestCase {

    public static final String LOGIN_BUTTON = "loginButton";
    public static final String USER_NAME_TEXT = "userNameText";
    public static final String PASSWORD_TEXT = "passwordText";
    private LaunchActivity activity;
    private Mock mock;

    @Before
    public void setUp() throws Exception {
        activity = new LaunchActivity();
        activity.onCreate(null);
        mock = new Mock(LoginController.class);
    }

    @Test
    public void shouldHaveUserInputPasswordInputAndALoginButton() throws Exception {
        View userInput = activity.findViewById(findIdForStringIdentifier(USER_NAME_TEXT));
        assertTrue("Expecting an EditText field", userInput instanceof EditText);
        View passwordInput = activity.findViewById(findIdForStringIdentifier(PASSWORD_TEXT));
        assertTrue("Expecting an EditText field", passwordInput instanceof EditText);
        View loginButton = activity.findViewById(findIdForStringIdentifier(LOGIN_BUTTON));
        assertTrue("Expecting a Button type, found: " + loginButton.getClass().getName(), loginButton instanceof Button);
    }

    @Test
    public void shouldAcceptLoginController() throws Exception {
        LoginController loginController = (LoginController) mock.proxy();
        activity.setLoginController(loginController);
    }

    @Test
    public void pressingLoginShouldInvokeLoginControllerWithUserNameAndPassword() throws Exception {
        //Given...
        /*our activity*/ shouldAcceptLoginController();
        //and we have...
        String testUserName = "any user name" + new Random().nextInt();
        String testPassword = "secret password"  + new Random().nextInt();
        setTextForEditTextField(testUserName, USER_NAME_TEXT);
        setTextForEditTextField(testPassword, PASSWORD_TEXT);
        /*our */mock.expects(once()).method("login").with(eq(testUserName),eq(testPassword)).will(returnValue(false));
        //When we..
        activity.findViewById(findIdForStringIdentifier(LOGIN_BUTTON)).performClick();
        mock.verify();
    }
    @Test
    public void validLoginLaunchesChatActivity() throws Exception {
        //Given...
        /*our activity*/ shouldAcceptLoginController();
        /*a */mock/*that*/.stubs().method("login").with(ANYTHING, ANYTHING)/*and*/.will(returnValue(true));
        //When we..
        activity.findViewById(findIdForStringIdentifier(LOGIN_BUTTON)).performClick();
        //Then we should...
        assertThat(nextStartedActivity(activity).getComponent().getClassName(), eq(ChatActivity.class.getName()));
    }

    private ShadowIntent nextStartedActivity(Activity anActivity) {
        ShadowActivity shadowActivity = shadowOf(anActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNotNull("Should have started a new intent.", startedIntent);
        return shadowOf(startedIntent);
    }

    private void setTextForEditTextField(String text, String editText) {
        EditText userName = (EditText) activity.findViewById(findIdForStringIdentifier(editText));
        userName.setText(text);
    }

    private int findIdForStringIdentifier(String viewId) {
        int id = activity.getResources().getIdentifier(viewId, "id", activity.getPackageName());
        assertFalse("Id for view '" + viewId + "' should not be 0.", 0==id);
        return id;
    }
}
