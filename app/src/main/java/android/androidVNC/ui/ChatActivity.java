package android.androidVNC.ui;

import android.androidVNC.R;
import android.androidVNC.adapters.MessageListAdapter;
import android.androidVNC.models.Message;
import android.androidVNC.models.User;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    public static List<Message> messageList = new ArrayList<Message>();
    EditText editTextChat;
    Button chatButton;
    User user1;
    User user2;

    private Socket client;
    private PrintWriter printwriter;
    private BufferedReader bufferedReader;
    private String CHAT_SERVER_IP = "192.168.173.1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        if (intent != null) {
            CHAT_SERVER_IP = intent.getStringExtra("ip");
        }
        user1 = new User();
        user1.setNickname("me");
        user1.setProfileUrl("Emem");

        user2 = new User();
        user1.setNickname("you");
        user1.setProfileUrl("Warrie");

        Message message =new Message();
        message.setCreatedAt(12345);
        message.setMessage("Ask your question");
        message.setSender(user1);

        messageList.add(message);

        editTextChat = findViewById(R.id.edittext_chatbox);
        chatButton = findViewById(R.id.button_chatbox_send);
        chatButton.setEnabled(false);

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageAdapter.notifyDataSetChanged();


        ChatOperator chatOperator = new ChatOperator();
        chatOperator.execute();

    }

    private class ChatOperator extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... arg0) {
        //
        Log.e("TAG", "do in background");
        try {
            Log.e("TAG", "try");
            client = new Socket(CHAT_SERVER_IP, 4447); // Creating the server socket.
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "catch");
        }

        try {

            Log.e("TAG", "tyr 2");
            if (client != null) {
                Log.e("TAG", "if");
                printwriter = new PrintWriter(client.getOutputStream(), true);
                InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                Log.e("TAG", "Server has started on port 4444.");
            } else {
                Log.e("TAG", "Server has not started on port 4444.");
                System.out.println("Server has not bean started on port 4444.");
            }
        } catch (UnknownHostException e) {
            Log.e("TAG", "Faild to connect h server " + CHAT_SERVER_IP);
            System.out.println("Faild to connect server " + CHAT_SERVER_IP);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("TAG", "Faild to connect server " + CHAT_SERVER_IP);
            System.out.println("Faild to connect server " + CHAT_SERVER_IP);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Following method is executed at the end of doInBackground method.
     */
    @Override
    protected void onPostExecute(Void result) {
        chatButton.setEnabled(true);
        chatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (editTextChat.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this,
                            "Empty message can't be sent", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Sender messageSender = new Sender(); // Initialize chat sender AsyncTask.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    messageSender.execute();
                }
            }
        });
        editTextChat.setText("");

        Receiver receiver = new Receiver(); // Initialize chat receiver AsyncTask.
        receiver.execute();

    }

}

/**
 * This AsyncTask continuously reads the input buffer and show the chat
 * message if a message is availble.
 */
private class Receiver extends AsyncTask<Void, Void, Void> {

    private String message;

    @Override
    protected Void doInBackground(Void... params) {
        while (true) {
            try {

                if(bufferedReader == null)
                    break;

                if (bufferedReader.ready()) {
                    message = bufferedReader.readLine();
                    publishProgress(null);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        Message newMessage = new Message();
        newMessage.setSender(user2);
        newMessage.setMessage(message);
        newMessage.setCreatedAt(1);

        messageList.add(newMessage);
        mMessageAdapter.notifyDataSetChanged();
    }

}

/**
 * This AsyncTask sends the chat message through the output stream.
 */
private class Sender extends AsyncTask<Void, Void, Void> {

    private String message;



    @Override
    protected Void doInBackground(Void... params) {

        printwriter.write(message + "\n");
        printwriter.flush();

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        message = editTextChat.getText().toString();
        editTextChat.setText("");
    }

    @Override
    protected void onPostExecute(Void result) {

        Message newMessage = new Message();
        newMessage.setSender(user1);
        newMessage.setMessage(message);
        newMessage.setCreatedAt(System.currentTimeMillis());

        messageList.add(newMessage);
        mMessageAdapter.notifyDataSetChanged();

    }
}
}
