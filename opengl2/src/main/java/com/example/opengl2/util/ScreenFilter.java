package com.example.opengl2.util;

import android.content.Context;
import android.opengl.GLES20;

import com.example.opengl2.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;

public class ScreenFilter {

    //句柄  gpu中  vPosition
    private int vPosition;
    FloatBuffer textureBuffer; // 纹理坐标
    private int vCoord;
    private int vTexture;
    private int vMatrix;
    private int mWidth;
    private int mHeight;
    private float[] mtx;
    //gpu顶点缓冲区
    FloatBuffer vertexBuffer; //顶点坐标缓存区
    //顶点坐标
    float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };
    //纹理坐标
    float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    private int program;

    public ScreenFilter(Context context) {
        //GPU中创建一个缓冲区，提供给CPU，然后数据通过这个放到缓冲区
        vertexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2)
                .order(ByteOrder.nativeOrder())  //重新整理下内存
                .asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2)
                .order(ByteOrder.nativeOrder())  //重新整理下内存
                .asFloatBuffer();
        textureBuffer.clear();
        textureBuffer.put(TEXTURE);

        String vertexSharder = OpenGLUtils.readRawTextFile(context, R.raw.camera_vert);
        String fragSharder = OpenGLUtils.readRawTextFile(context, R.raw.camera_frag2);

        //给gpu使用的
        program = OpenGLUtils.loadProgram(vertexSharder, fragSharder);

        vPosition = GLES20.glGetAttribLocation(program, "vPosition");//0
        //接收纹理坐标，接收采样器采样图片的坐标
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");//1
        //采样点的坐标
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");

        //变换矩阵， 需要将原本的vCoord（01,11,00,10） 与矩阵相乘
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    //摄像头数据，开始渲染
    public void onDraw(int texture) {
//      View 的大小
        GLES20.glViewport(0, 0, mWidth, mHeight);
//        使用程序
        GLES20.glUseProgram(program);

//      从索引为0的地方读
        vertexBuffer.position(0);
//     index 指定要修改的通用顶点属性的索引。
//     size  指定每个通用顶点属性的组件数。
//     type  指定数组中每个组件的数据类型。
//        接受符号常量GL_FLOAT  GL_BYTE，GL_UNSIGNED_BYTE，GL_SHORT，GL_UNSIGNED_SHORT或GL_FIXED。 初始值为GL_FLOAT。
//     normalized    指定在访问定点数据值时是应将其标准化（GL_TRUE）还是直接转换为定点值（GL_FALSE）。
//     stride 步长
//      使句柄和数据绑定
        GLES20.glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 0, vertexBuffer);
//      vPosition生效
        GLES20.glEnableVertexAttribArray(vPosition);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GL_FLOAT, false, 0, textureBuffer);
        //CPU传数据到GPU，默认情况下着色器无法读取到这个数据。 需要我们启用一下才可以读取
        GLES20.glEnableVertexAttribArray(vCoord);

//        激活图层
        GLES20.glActiveTexture(GL_TEXTURE0);

//      生成一个采样
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(vTexture, GL_TEXTURE0);
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
//      通知渲染
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
