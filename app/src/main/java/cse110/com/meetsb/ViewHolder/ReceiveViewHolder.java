package cse110.com.meetsb.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;
import cse110.com.meetsb.Model.Message;
import cse110.com.meetsb.R;

public class ReceiveViewHolder extends RecyclerView.ViewHolder {
    TextView my_message;
    TextView showTime;
    public ReceiveViewHolder(@NonNull View itemView) {
        super(itemView);
        //my_message=(TextView)itemView.findViewById(R.id.their_message_body);
        //showTime=(TextView)itemView.findViewById(R.id.time_show);
        my_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showTime.getVisibility()==View.GONE) {
                    showTime.setVisibility(View.VISIBLE);
                }
                else{
                    showTime.setVisibility(View.GONE);
                }
            }
        });
    }
    public void bindToMessage(Message message){
        PrettyTime p = new PrettyTime();
        showTime.setText(p.format(new Date((long)message.getTime())));
        my_message.setText(message.getText());
    }
}
