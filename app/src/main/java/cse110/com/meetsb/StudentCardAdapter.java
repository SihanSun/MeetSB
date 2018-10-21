package cse110.com.meetsb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.List;

public class StudentCardAdapter extends BaseAdapter {
    private Context mContext;
    private List<StudentCardMode> mCardList;

    public StudentCardAdapter(Context mContext, int item, int buddyName, List<StudentCardMode> mCardList) {
        this.mContext = mContext;
        this.mCardList = mCardList;
    }

    public int getCount() {
        return mCardList.size();
    }

    public Object getItem(int position) {
        return mCardList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        StudentCardAdapter.ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
            holder = new StudentCardAdapter.ViewHolder();
            holder.mCardImageView = (ImageView) convertView.findViewById(R.id.buddyName);
            holder.mCardName = (TextView) convertView.findViewById(R.id.card_name);
            holder.mCardImageNum = (TextView) convertView.findViewById(R.id.card_image_num);
            holder.mCardYear = (TextView) convertView.findViewById(R.id.card_year);
            convertView.setTag(holder);
        } else {
            holder = (StudentCardAdapter.ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(mCardList.get(position).getImages().get(0))
                .into(holder.mCardImageView);
        holder.mCardName.setText(mCardList.get(position).getName());
        holder.mCardYear.setText(String.valueOf(mCardList.get(position).getYear()));
        return convertView;
    }

    class ViewHolder {
        TextView mCardName;
        TextView mCardYear;
        TextView mCardImageNum;
        ImageView mCardImageView;
    }
}
