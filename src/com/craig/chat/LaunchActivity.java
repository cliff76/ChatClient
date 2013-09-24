package com.craig.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LaunchActivity extends Activity {
    private LoginController loginController;

    public LaunchActivity() {
        setLoginController(new SimpleLoginController());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onLogin(View sender) {
        String userName = ((EditText) findViewById(R.id.userNameText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        if (this.loginController.login(userName, password)) {
            startActivityForClass(ChatActivity.class);
        } else {
            View errorDialog = findViewById(R.id.errorDialog);
            errorDialog.setVisibility(View.VISIBLE);
            ((TextView) errorDialog.findViewById(R.id.errorMessageText))
                    .setText("Invalid login credentials! Try again.");
        }
    }

    private void startActivityForClass(Class<ChatActivity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
