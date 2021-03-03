package com.example.opengl2.fbo;

import android.content.Context;
import android.opengl.GLES20;

import com.example.opengl2.R;
import com.example.opengl2.base.AbstractFboFilter;

public class CameraFboFilter extends AbstractFboFilter {

    protected int vMatrix;
    private float[] mtx;

    public CameraFboFilter(Context context) {
//        super(context, R.raw.base_vert, R.raw.base_frag);
        super(context, R.raw.camera_vert, R.raw.camera_frag);
        //变换矩阵， 需要将原本的vCoord（01,11,00,10） 与矩阵相乘
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

    @Override
    public void beforeDraw() {
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
    }

}
