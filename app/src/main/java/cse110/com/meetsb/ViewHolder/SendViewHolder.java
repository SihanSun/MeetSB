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

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Date;
import cse110.com.meetsb.Model.Message;
import cse110.com.meetsb.Model.TextType;
import cse110.com.meetsb.R;


public class SendViewHolder extends RecyclerView.ViewHolder{
    TextView my_message;
    TextView showTime;
    ImageView my_profile;
    ImageView my_image;
    private Context context;

    public SendViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        my_message=(TextView)itemView.findViewById(R.id.my_message_body);
        showTime=(TextView)itemView.findViewById(R.id.time_show);
        my_profile=(ImageView)itemView.findViewById(R.id.image);
        my_image=(ImageView)itemView.findViewById(R.id.my_image_body);
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
        if (message.getTextType().equals(TextType.text)) {
            my_message.setText(message.getText());
        }
        else{
            Glide.with(context).load(message.getText()).into(my_image);
        }
        //my_profile.setImageURI(Uri.parse(message.getProfileImage()));
       Glide.with(context).load(message.getProfileImage()).into(my_profile);
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
