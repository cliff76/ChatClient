package com.craig.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: clcraig
* Date: 2/19/13
* Time: 9:40 AM
* To change this template use File | Settings | File Templates.
*/
class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    List<String> chatMessages = new ArrayList<String>();
    private ChatActivity chatActivity;

    public ChatMessageAdapter(ChatActivity chatActivity, Context context) {
        this.chatActivity = chatActivity;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return this.chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null==convertView) {
            convertView = new TextView(this.context);
        }
        ((TextView) convertView).setText((String)getItem(position));
        return convertView;
    }

    public void addMessage(String message) {
        this.chatMessages.add(message);
        chatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public List<String> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<String> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
