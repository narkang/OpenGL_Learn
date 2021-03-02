package com.example.opengl2.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.opengl2.render.CameraRender;

public class CameraView extends GLSurfaceView {

    private CameraRender render;

    public CameraView(Context context) {
        super(context);

        onInit(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        onInit(context);
    }

    private void onInit(Context context){
        //GLContext设置OpenGLES2.0
        setEGLContextClientVersion(2);
        render = new CameraRender(this);
        setRenderer(render);
        /*渲染方式，RENDERMODE_WHEN_DIRTY表示被动渲染，只有在调用requestRender或者onResume等方法时才会进行渲染。RENDERMODE_CONTINUOUSLY表示持续渲染*/
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


}
