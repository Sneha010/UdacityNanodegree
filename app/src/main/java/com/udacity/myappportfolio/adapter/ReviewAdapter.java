package com.udacity.myappportfolio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> mItems;
    private Context context;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        super();
        mItems = reviewList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_row_item_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Review reviewObj = mItems.get(i);

        viewHolder.tv_AuthorName.setText(reviewObj.getAuthor());
        viewHolder.tv_ReviewDesc.setText(reviewObj.getContent());

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_AuthorName, tv_ReviewDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_AuthorName = (TextView) itemView.findViewById(R.id.tv_AuthorName);
            tv_ReviewDesc = (TextView) itemView.findViewById(R.id.tv_ReviewDesc);
        }
    }
}
