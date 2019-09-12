package com.mab.relaxator;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    RecyclerItemClickListener(Context context, OnItemClickListener onItemClickListener){

            this.mListener = (OnItemClickListener) onItemClickListener;
            this.mGestureDetector = new GestureDetector(context,(GestureDetector.OnGestureListener) new GestureDetector.SimpleOnGestureListener(){
            public boolean onSingleTapUp(MotionEvent motionEvent){
                return true;
            }
            });


    }

    public  interface OnItemClickListener{
        public void onItemClick(View view1 , int position);
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        if(view != null && this.mListener != null && this.mGestureDetector.onTouchEvent(motionEvent)){
            this.mListener.onItemClick(view,recyclerView.getChildAdapterPosition(view));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

}
