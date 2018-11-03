package cse110.com.meetsb.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ChatAdapter extends FirebaseRecyclerAdapter<Chat,ChatAdapter.MyChatViewHolder> {
    private String userId;
    private ChatClickListener chatClickListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirebaseRecyclerOptions<Chat> options) {
        super(options);
    }


    @Override
    public MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyChatViewHolder holder, int position, @NonNull Chat model) {

    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyChatViewHolder(View itemView) {
            super(itemView);
        }


        @Override
        public void onClick(View view) {
        }
    }
}
