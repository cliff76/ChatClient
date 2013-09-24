package com.craig.chat;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
 * User: Clifton Craig
 * Date: 2/19/13
 * Time: 11:15 AM
 */
@RunWith(RobolectricTestRunner.class)
public class LoginTest extends MockObjectTestCase {

    public static final String LOGIN_BUTTON = "loginButton";
    public static final String USER_NAME_TEXT = "userNameText";
    public static final String PASSWORD_TEXT = "passwordText";
    public static final String ERROR_DIALOG = "errorDialog";
    public static final String ERROR_MESSAGE_TEXT = "errorMessageText";
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

        //When we..
        activity.setLoginController(anAlwaysValidLoginController());
        activity.findViewById(findIdForStringIdentifier(LOGIN_BUTTON)).performClick();
        //Then we should...
        assertThat(nextStartedActivity(activity).getComponent().getClassName(), eq(ChatActivity.class.getName()));
    }

    @Test
    public void anInvalidLoginPresentsErrorDialog() throws Exception {
        /*Given our...*/ activityHasAnErrorDialogThatDoesNotShow();

        //When we..
        /*ask our*/activity/*to*/.setLoginController(/*to*/anAlwaysInValidLoginController());
        //And we...
        /*ask our*/activity/*to*/
                .findViewById(/*after we*/findIdForStringIdentifier(LOGIN_BUTTON))/*and*/.performClick();//on the button
        //Then...
        View errorDialog = activity.findViewById(findIdForStringIdentifier(ERROR_DIALOG));
        assertTrue("The error dialog should be visible", errorDialog.getVisibility()==View.VISIBLE);
        //And...
        assertFalse("The error message SHOULD include error text",
                ((TextView) errorDialog.findViewById(findIdForStringIdentifier(ERROR_MESSAGE_TEXT)))
                        .getText().equals(""));
    }

    @Test
    public void activityHasAnErrorDialogThatDoesNotShow() {
        //When we look for our error dialog...
        View errorDialog = activity.findViewById(findIdForStringIdentifier(ERROR_DIALOG));
        //Then...
        assertTrue("Error dialog should not be visible", errorDialog.getVisibility()==View.GONE);
        //And we find our errorMessage text view...
        View errorMessage = errorDialog.findViewById(findIdForStringIdentifier(ERROR_MESSAGE_TEXT));
        //And...
        assertTrue("The errorMessage should be a TextView", errorMessage instanceof TextView);
        //And...
        assertTrue("The error message should not currently include any error text",
                ((TextView) errorMessage).getText().equals(""));
    }

    private LoginController anAlwaysValidLoginController() {
        return aLoginControllerThatReturnsConstant(true);
    }

    private LoginController anAlwaysInValidLoginController() {
        return aLoginControllerThatReturnsConstant(false);
    }

    private LoginController aLoginControllerThatReturnsConstant(boolean result) {
    /*a */
        mock/*that*/.stubs().method("login").with(ANYTHING, ANYTHING)
        /*and*/.will(/*always*/returnValue(result));
        return (LoginController) mock.proxy();
    }

    private ShadowIntent nextStartedActivity(Activity anActivity) {
        ShadowActivity shadowActivity = shadowOf(anActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNotNull(anActivity.getClass().getName() + " should DID NOT start a new intent.", startedIntent);
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
