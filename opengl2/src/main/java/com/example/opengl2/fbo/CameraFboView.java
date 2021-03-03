package com.example.opengl2.fbo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.opengl2.render.CameraRender;

public class CameraFboView extends GLSurfaceView {

    private CameraFboRender render;

    public CameraFboView(Context context) {
        super(context);

        onInit(context);
    }

    public CameraFboView(Context context, AttributeSet attrs) {
        super(context, attrs);

        onInit(context);
    }

    private void onInit(Context context){
        //GLContext设置OpenGLES2.0
        setEGLContextClientVersion(2);
        render = new CameraFboRender(this);
        setRenderer(render);
        /*渲染方式，RENDERMODE_WHEN_DIRTY表示被动渲染，只有在调用requestRender或者onResume等方法时才会进行渲染。RENDERMODE_CONTINUOUSLY表示持续渲染*/
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private Speed mSpeed = Speed.MODE_NORMAL;

    public enum Speed {
        MODE_EXTRA_SLOW, MODE_SLOW, MODE_NORMAL, MODE_FAST, MODE_EXTRA_FAST
    }
    public void setSpeed(Speed speed) {
        this.mSpeed = speed;
    }

    public void startRecord(){
        //速度  时间/速度 speed小于就是放慢 大于1就是加快
        float speed = 1.f;
        switch (mSpeed) {
            case MODE_EXTRA_SLOW:
                speed = 0.3f;
                break;
            case MODE_SLOW:
                speed = 0.5f;
                break;
            case MODE_NORMAL:
                speed = 1.f;
                break;
            case MODE_FAST:
                speed = 2.f;
                break;
            case MODE_EXTRA_FAST:
                speed = 3.f;
                break;
        }
        render.startRecord(speed);
    }
    public void stopRecord(){
        render.stopRecord();
    }
}
