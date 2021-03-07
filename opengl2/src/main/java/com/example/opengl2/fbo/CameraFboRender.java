package com.example.opengl2.fbo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.example.opengl2.util.CameraHelper;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraFboRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "CameraRender";
    private CameraHelper cameraHelper;
    private CameraFboView cameraView;
    private SurfaceTexture mCameraTexture;
    private int[] textures;
    float[] mtx = new float[16];
    private CameraFboFilter screenFilter;
    private RecordFilter recordFilter;
    private SoulFilter soulFilter;

    private MediaRecorder mRecorder;

    public CameraFboRender(CameraFboView cameraView) {
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

        screenFilter = new CameraFboFilter(cameraView.getContext());

        Context context = cameraView.getContext();
        recordFilter = new RecordFilter(context);
        soulFilter = new SoulFilter(context);

        String path = new File(Environment.getExternalStorageDirectory(),
                "input.mp4").getAbsolutePath();

        mRecorder = new MediaRecorder(context, path,
                EGL14.eglGetCurrentContext(),
                480, 640);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenFilter.setSize(width, height);  //初始化完成
        recordFilter.setSize(width, height);
        soulFilter.setSize(width, height);
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

        //id  fbo所在图层
        int id = screenFilter.onDraw(textures[0]);
        //制作特效
        id = soulFilter.onDraw(id);
//      显示到屏幕
        id = recordFilter.onDraw(id);
//      id代表数据,传入当前录制的时间搓，主动拿数据
        mRecorder.findFrame(id, mCameraTexture.getTimestamp());
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

    public void startRecord(float speed) {
        try {
            mRecorder.start(speed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopRecord() {
        mRecorder.stop();
    }
}
