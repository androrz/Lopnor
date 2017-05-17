package com.androrz.lopnor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androrz.lopnor.model.Element;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Element> mDataSets;
    private Context mContext;

    public MainAdapter(Context context, List<Element> data) {
        mDataSets = data;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView mNumber;
        public final TextView mShortName;
        public final TextView mWholeName;


        public ViewHolder(View itemView) {
            super(itemView);
            mNumber = (TextView) itemView.findViewById(R.id.number);
            mShortName = (TextView) itemView.findViewById(R.id.short_name);
            mWholeName = (TextView) itemView.findViewById(R.id.whole_name);
            view = itemView;
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Element e = mDataSets.get(position);
        holder.mNumber.setText(e.number);
        holder.mShortName.setText(e.shortName);
        holder.mWholeName.setText(e.wholeName);
        String colorStr = "#" + e.config;
        if (colorStr.equals("#")) {
            colorStr = "#FFFFFF";
        }
        int color = Color.parseColor(colorStr);
        holder.view.setBackgroundColor(color);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class<?> elementClass = Class.forName("com.androrz.lopnor.elements." + e.shortName);
                    mContext.startActivity(new Intent(mContext, elementClass));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSets.size();
    }
}
