package com.udacity.myappportfolio.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.model.Trailer;
import com.udacity.myappportfolio.util.Constants;

import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> mItems;
    private Context context;

    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        super();
        mItems = trailerList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_row_item_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Trailer trailerObj = mItems.get(i);

        viewHolder.tv_TrailerDesc.setText(trailerObj.getName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.YOUTUBE_BASE_URL + trailerObj.getKey();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_TrailerDesc;
        public ImageView iv_TrailerIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_TrailerDesc = (TextView) itemView.findViewById(R.id.tv_TrailerDesc);
            iv_TrailerIcon = (ImageView) itemView.findViewById(R.id.iv_TrailerIcon);
        }
    }
}
