package cse110.com.meetsb.ViewHolder;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;
import cse110.com.meetsb.Model.Message;
import cse110.com.meetsb.Model.TextType;
import cse110.com.meetsb.R;

public class ReceiveViewHolder extends RecyclerView.ViewHolder {
    TextView my_message;
    TextView showTime;
    ImageView your_profile;
    ImageView your_image;
    Context context;
    public ReceiveViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        my_message=(TextView)itemView.findViewById(R.id.your_message_body);
        showTime=(TextView)itemView.findViewById(R.id.time_show);
        your_profile = (ImageView)itemView.findViewById(R.id.avatar);
        your_image = (ImageView)itemView.findViewById(R.id.your_image_body);
        this.context = context;
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
        //your_profile.setImageURI(Uri.parse(message.getProfileImage()));
        if (message.getTextType().equals(TextType.text)) {
            my_message.setText(message.getText());
        }
        else{
            Glide.with(context).load(message.getText()).into(your_image);
        }
        Glide.with(context).load(message.getProfileImage()).into(your_profile);
    }
    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }
}
