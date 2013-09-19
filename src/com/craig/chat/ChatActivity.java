package com.craig.chat;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: clcraig
 * Date: 2/17/13
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChatActivity extends ListActivity {
    public static final String CHAT_MESSAGES = "chatMessages";
    private EditText sendMessage;
    private ChatterBot bot;
    private ChatterBotSession botSession;
    private ChatMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        this.sendMessage = (EditText) findViewById(R.id.sendMessage);
        adapter = new ChatMessageAdapter(this, this);
        this.setListAdapter(adapter);
        getChatMessageAdapter().addMessage("Connecting to chat...");
        connectToChatbot();
    }

    private void connectToChatbot() {
        try {bot = new ChatterBotFactory().create(ChatterBotType.JABBERWACKY);}
        catch (Exception e) {throw new RuntimeException("Exception caught: " + e, e);}
        botSession = bot.createSession();
        getChatMessageAdapter().addMessage("Connected!");
    }

    public void onSend(View sender) {
        sendYourMessage(this.sendMessage.getText().toString());
        this.sendMessage.setText("");
    }

    private void sendYourMessage(String yourMessage) {
        getChatMessageAdapter().addMessage("You: " + yourMessage);
        sendToBot(yourMessage);
    }

    private void sendBuddyMessage(String buddyMessage) {
        getChatMessageAdapter().addMessage("Jabberwacky: " + buddyMessage);
    }

    private void sendToBot(final String yourMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String buddyMessage;
                try {buddyMessage = botSession.think(yourMessage);}
                catch (Exception e) {
                    throw new RuntimeException("Exception caught: " + e,e);
                }
                sendBuddyMessage(buddyMessage);
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(CHAT_MESSAGES, new ArrayList<String>(getChatMessageAdapter().getChatMessages()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        getChatMessageAdapter().setChatMessages(state.getStringArrayList(CHAT_MESSAGES));
    }

    private ChatMessageAdapter getChatMessageAdapter() {
        return adapter;
    }
}
