package com.example.opengl2.fbo;

import android.content.Context;

import com.example.opengl2.R;
import com.example.opengl2.base.AbstractFboFilter;

public class SplitFilter extends AbstractFboFilter{

    public SplitFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.split_screen);



    }
}
