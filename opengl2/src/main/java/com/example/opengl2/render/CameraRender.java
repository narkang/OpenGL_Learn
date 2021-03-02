package com.example.opengl2.render;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Log;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.example.opengl2.util.CameraHelper;
import com.example.opengl2.util.ScreenFilter;
import com.example.opengl2.view.CameraView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "CameraRender";
    private CameraHelper cameraHelper;
    private CameraView cameraView;
    private SurfaceTexture mCameraTexture;
    private  int[] textures;
    float[] mtx = new float[16];
    private ScreenFilter screenFilter;

    public CameraRender(CameraView cameraView) {
        this.cameraView = cameraView;
        LifecycleOwner lifecycleOwner = (LifecycleOwner) cameraView.getContext();
        cameraHelper = new CameraHelper(lifecycleOwner, this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        textures = new int[1];

        //让SurfaceTexture与Gpu共享一个数据源  0-31
        mCameraTexture.attachToGLContext(textures[0]);
        //监听摄像头数据回调
        mCameraTexture.setOnFrameAvailableListener(this);

        screenFilter = new ScreenFilter(cameraView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenFilter.setSize(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e(TAG, "线程: " + Thread.currentThread().getName());
//      更新摄像头的数据，给了gpu
        mCameraTexture.updateTexImage();
//      不是数据，给纹理坐标转换的
        mCameraTexture.getTransformMatrix(mtx);
        screenFilter.setTransformMatrix(mtx);
//      开始绘制
        screenFilter.onDraw(textures[0]);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //一帧一帧回调时，手动渲染
        cameraView.requestRender();
    }

    @Override
    public void onUpdated(Preview.PreviewOutput output) {
//      摄像头预览到的数据 在这里
        mCameraTexture = output.getSurfaceTexture();
    }
}
