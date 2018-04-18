package com.example.yinp.gank.view.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yinp.gank.R;

/**
 * @author yinp
 * https://github.com/XRecyclerView/XRecyclerView/blob/master/xrecyclerview/src/main/java/com/jcodecraeer/xrecyclerview/LoadingMoreFooter.java
 * todo: 对照优化下
 */
public class LoadingMoreFooter extends LinearLayout {
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;

    private TextView mTextView;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.xrecyclerview_footer, this);
        mTextView = findViewById(R.id.xrecyclerview_msg);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mTextView.setText(getContext().getText(R.string.xrecyclerview_loading_more));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mTextView.setText(getContext().getText(R.string.xrecyclerview_loading_more));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mTextView.setText(getContext().getText(R.string.xrecyclerview_nomore_loading));
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void reset() {
        this.setVisibility(GONE);
    }
}
