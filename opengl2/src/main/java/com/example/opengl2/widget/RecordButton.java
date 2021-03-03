package com.example.opengl2.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.logging.Handler;

public class RecordButton extends AppCompatTextView {

    private OnRecordListener mListener;

    public RecordButton(Context context) {
        super(context);

        setClick();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClick();
    }

    private void setClick() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) {
                    return;
                }

                mListener.onRecordStart();

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onRecordStop();
                    }
                }, 5000);
            }
        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (mListener == null) {
//            return false;
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mListener.onRecordStart();
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mListener.onRecordStop();
//                break;
//        }
//        return true;
//    }


    public void setOnRecordListener(OnRecordListener listener) {
        mListener = listener;
    }

    public interface OnRecordListener {
        void onRecordStart();

        void onRecordStop();
    }
}
