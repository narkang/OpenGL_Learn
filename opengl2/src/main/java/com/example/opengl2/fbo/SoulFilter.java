package com.example.opengl2.fbo;

import android.content.Context;
import android.opengl.GLES20;

import com.example.opengl2.R;
import com.example.opengl2.base.AbstractFboFilter;

public class SoulFilter extends AbstractFboFilter {

    private int scalePercent;
    private float scale = 0.0f; //缩放，越大就放得越大
    private int mixturePercent;
    private float mix = 0.0f; //透明度，越大越透明

    public SoulFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.soul_fragment);

        //拿到这个值的句柄
        scalePercent = GLES20.glGetUniformLocation(program, "scalePercent");
        mixturePercent = GLES20.glGetUniformLocation(program, "mixturePercent");
    }

    @Override
    public void beforeDraw() {

        GLES20.glUniform1f(scalePercent, scale + 1.0f);
        GLES20.glUniform1f(mixturePercent, 1.0f - mix);

        scale += 0.08f;
        if(scale >= 1.0){
            scale = 0.0f;
        }

        mix += 0.08f;
        if(mix >= 1.0f){
            mix = 0.0f;
        }
    }
}
