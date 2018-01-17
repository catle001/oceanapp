package com.example.user.fypapp.Presenter;

/**
 * Created by user on 24/2/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.fypapp.Model.App;
import com.example.user.fypapp.R;
import com.example.user.fypapp.View.DetailActivity;

import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private Context mContext;
    public ArrayList<App> mDataSet;
    private static LayoutInflater inflater = null;


    public ListAdapter(Context context, ArrayList<App> list) {
        mContext = context;
        mDataSet = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        TextView title;
        ImageView thumb_image;
        Bitmap imgbit;
        protected ProgressBar pb;

        public ViewHolder(View v) {
            super(v);
            // Get the widget reference from the custom layout
            mCardView = (CardView) v.findViewById(R.id.card_view);
            title = (TextView) v.findViewById(R.id.title);
            thumb_image = (ImageView) v.findViewById(R.id.list_image);
            pb = (ProgressBar) v.findViewById(R.id.progressBar1);

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        App clickedDataItem = mDataSet.get(pos);
                        Intent mIntent = new Intent(v.getContext(), DetailActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("id", clickedDataItem.getAppId());
                        mIntent.putExtras(mBundle);
                        v.getContext().startActivity(mIntent);
                    }
                }
            });
        }
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.custom_card, parent, false);
        ViewHolder vh = new ViewHolder(v);

        // Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        App application = mDataSet.get(position);
        holder.title.setText(application.getTitle());
        holder.imgbit = application.getImgbit();


        if (!application.getIcon().contains("http")) {
            application.setIcon("http:" + application.getIcon());
        }


        if (holder.imgbit != null) {
            holder.thumb_image.setVisibility(View.VISIBLE);
            holder.pb.setVisibility(View.GONE);
            holder.thumb_image.setImageBitmap(holder.imgbit);
        } else {
            new ImageLoader(application, holder.thumb_image, holder.pb).execute();
        }


    }


    @Override
    public int getItemCount() {
        // Count the items
        return mDataSet.size();
    }
}