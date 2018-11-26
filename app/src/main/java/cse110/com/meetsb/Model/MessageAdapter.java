package cse110.com.meetsb.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import cse110.com.meetsb.R;
import cse110.com.meetsb.ViewHolder.ReceiveViewHolder;
import cse110.com.meetsb.ViewHolder.SendViewHolder;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final LinkedList<Message> messageList;
    private final Context context;
    private final int SEND_VIEW = 0;
    private final int RECEIVE_VIEW = 1;

    public MessageAdapter(Context context, LinkedList<Message> messageList){
        this.context = context;
        this.messageList = messageList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sendView = LayoutInflater.from(context).inflate(R.layout.my_message, parent, false);
        View receiveView = LayoutInflater.from(context).inflate(R.layout.your_message, parent, false);

        if(viewType == SEND_VIEW){
            SendViewHolder sendViewHolder = new SendViewHolder(sendView);
            return sendViewHolder;
        }

        else {
            ReceiveViewHolder receiveViewHolder = new ReceiveViewHolder(receiveView);
            return receiveViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Message message = messageList.get(i);
        switch(viewHolder.getItemViewType()){
            case SEND_VIEW:
                final SendViewHolder sendViewHolder = (SendViewHolder) viewHolder;
                sendViewHolder.bindToMessage(message);
                break;
            case RECEIVE_VIEW:
                final ReceiveViewHolder receiveViewHolder = (ReceiveViewHolder) viewHolder;
                receiveViewHolder.bindToMessage(message);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message= messageList.get(position);
        if(message.getMessageType().equals(MessageType.me)){
            return SEND_VIEW;
        }
        else{
            return RECEIVE_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
