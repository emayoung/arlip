package android.androidVNC.adapters;

import android.androidVNC.R;
import android.androidVNC.models.Message;
import android.androidVNC.models.User;
import android.androidVNC.ui.ChatActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bless on 10/15/2017.
 */

public class MessageListAdapter extends RecyclerView.Adapter {


    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;

    public MessageListAdapter(Context context, List<Message> messageList) {

        mContext = context;
    }

    @Override
    public int getItemCount() {
        return ChatActivity.messageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) ChatActivity.messageList.get(position);

        if(position == 0){
            User user1 = new User();
            user1.setNickname("me");
            user1.setProfileUrl("Emem");

            message.setCreatedAt(System.currentTimeMillis());
            message.setMessage("Ask your question");
            message.setSender(user1);
            if (message.getCreatedAt() == 1 ) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }
        if(message == null){
            Log.e("TAG", "Message is null " );

        }else {
            Log.e("TAG", "Message is not null " + message.getSender().getNickname() + " with size " + ChatActivity.messageList.size());
        }


        if (message.getCreatedAt() == 1 ) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_SENT;
        }

    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_bubble, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("TAG", "bind viewholder0");
        Message message = (Message) ChatActivity.messageList.get(position);

        Log.e("TAG", "message " + message.getMessage());

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                Log.e("TAG", "holder  " + VIEW_TYPE_MESSAGE_SENT);
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                Log.e("TAG", "holder  " + VIEW_TYPE_MESSAGE_RECEIVED);
        }

        holder.isRecyclable();
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);

        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText("Today");

        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.sent_msg_by_me);

        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

        }
    }

}
