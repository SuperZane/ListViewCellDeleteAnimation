package com.example.myanimtest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    static final int ANIMATION_DURATION = 200;
    private static List<MyCell> mAnimList = new ArrayList<MyCell>();
    private MyAnimListAdapter mMyAnimListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0;i<50;i++) {
            MyCell cell = new MyCell();
            cell.name = "Cell No."+Integer.toString(i);
            mAnimList.add(cell);
        }

        mMyAnimListAdapter = new MyAnimListAdapter(this, R.layout.chain_cell, mAnimList);
        ListView myListView = (ListView) findViewById(R.id.chainListView);
        myListView.setAdapter(mMyAnimListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void deleteCell(final View v, final int index) {
        collapse(v);
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime >= 1) {
                    v.setVisibility(View.GONE);
                    // TODO remove item from adapter here
                }
                else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }


    private class MyCell {
        public String name;
    }

    private class ViewHolder {
        public TextView text;
        ImageButton imageButton;
    }

    public class MyAnimListAdapter extends ArrayAdapter<MyCell> {
        private LayoutInflater mInflater;
        private int resId;

        public MyAnimListAdapter(Context context, int textViewResourceId, List<MyCell> objects) {
            super(context, textViewResourceId, objects);
            this.resId = textViewResourceId;
            this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            ViewHolder vh;
            MyCell cell = (MyCell)getItem(position);

            if (convertView==null) {
                view = mInflater.inflate(R.layout.chain_cell, parent, false);
                setViewHolder(view);
            } else {
                view = convertView;
            }

            vh = (ViewHolder)view.getTag();
            vh.text.setText(cell.name);
            vh.imageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCell(view, position);
                }
            });
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
                if (view.getLayoutParams() != null) {
                    view.getLayoutParams().height = LayoutParams.MATCH_PARENT;
                }
            }
            return view;
        }

        private void setViewHolder(View view) {
            ViewHolder vh = new ViewHolder();
            vh.text = (TextView)view.findViewById(R.id.cell_name_textview);
            vh.imageButton = (ImageButton) view.findViewById(R.id.cell_trash_button);
            view.setTag(vh);
        }
    }
}
