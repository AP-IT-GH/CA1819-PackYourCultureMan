package com.ap.pacyourcultureman.Helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ap.pacyourcultureman.Assignment;
import com.ap.pacyourcultureman.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


public class SightsAdapter extends RecyclerView.Adapter<SightsAdapter.SightsViewHolder>{
    private Context mContext;
    private ArrayList<Assignment> mSightList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setonItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public SightsAdapter(Context context,ArrayList<Assignment> sightList){
        mContext = context;
        mSightList = sightList;
    }

    @Override
    public SightsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.sight_item,parent,false);
        return new SightsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SightsViewHolder sightsViewHolder, int position) {
            Assignment currentItem = mSightList.get(position);

            String imageUrl = currentItem.getImgUrl();
            String sightName = currentItem.getName();

            Picasso.get().load(imageUrl).fit().centerInside().into(sightsViewHolder.mImageView);
            sightsViewHolder.mTextviewName.setText(sightName);
    }

    @Override
    public int getItemCount() {
        return mSightList.size();
    }

    public class  SightsViewHolder extends  RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextviewName;
        public SightsViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView_sight);
            mTextviewName = itemView.findViewById(R.id.textView_sightName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
