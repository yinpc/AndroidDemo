package com.example.yinp.gank.view.statusbar;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yinp.gank.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<String> mTitles;

    public MyRecyclerViewAdapter(Context context, ArrayList<String> titles) {
//        mTitles = context.getResources().getStringArray(R.array.titles);
        mTitles = titles;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.recyclerview_textview, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mTitles.get(position));
        int normalMargin = dp2px(mContext, 8);
        int diffMargin = dp2px(mContext, 100);
        if (position == 0) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(holder.mCardView.getLayoutParams());
            lp.setMargins(normalMargin, diffMargin, normalMargin, normalMargin);
            holder.mCardView.setLayoutParams(lp);
        }
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view);
            mTextView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("NormalTextViewHolder", "onClick--> position = " + getLayoutPosition());
                    Toast.makeText(mContext, "click", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
