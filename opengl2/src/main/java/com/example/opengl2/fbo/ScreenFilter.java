package com.example.opengl2.fbo;

import android.content.Context;

import com.example.opengl2.R;
import com.example.opengl2.base.AbstractFilter;

public class ScreenFilter extends AbstractFilter {
    public ScreenFilter(Context context){
        super(context, R.raw.base_vert, R.raw.base_frag);
    }

}
