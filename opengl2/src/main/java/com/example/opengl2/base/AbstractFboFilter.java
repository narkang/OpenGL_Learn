package com.example.opengl2.base;

import android.content.Context;
import android.opengl.GLES20;

public class AbstractFboFilter extends AbstractFilter{

    int[] frameBuffer;
    int[] frameTextures;

    public AbstractFboFilter(Context context, int vertexSharderId, int fragSharderId) {
        super(context, vertexSharderId, fragSharderId);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        releaseFrame();

        //frameBuffer 是图层的一个引用
        frameBuffer = new int[1];
//        实例化fbo,让摄像头的数据先渲染到fbo
        GLES20.glGenFramebuffers(1, frameBuffer, 0);

//        生成一个纹理，相当于图层
        frameTextures = new int[1];
        GLES20.glGenTextures(frameTextures.length, frameTextures, 0);

        // 配置纹理
        for (int i = 0; i < frameTextures.length; i++) {
            //绑定纹理，后续配置纹理，开始操作纹理，后续操作是原子操作
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTextures[i]);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
//                    GLES20.GL_NEAREST);//放大过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);//缩小过滤
            //告诉，gpu操作完了
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }

//        开始做绑定操作
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTextures[0]);

        /**
         * 指定一个二维的纹理图片显示方式
         * level
         *     指定细节级别，0级表示基本图像，n级则表示Mipmap缩小n级之后的图像（缩小2^n）
         * internalformat
         *     指定纹理内部格式，必须是下列符号常量之一：GL_ALPHA，GL_LUMINANCE，GL_LUMINANCE_ALPHA，GL_RGB，GL_RGBA。
         * width height
         *     指定纹理图像的宽高，所有实现都支持宽高至少为64 纹素的2D纹理图像和宽高至少为16 纹素的立方体贴图纹理图像 。
         * border
         *     指定边框的宽度。必须为0。
         * format
         *     指定纹理数据的格式。必须匹配internalformat。下面的符号值被接受：GL_ALPHA，GL_RGB，GL_RGBA，GL_LUMINANCE，和GL_LUMINANCE_ALPHA。
         * type
         *     指定纹理数据的数据类型。下面的符号值被接受：GL_UNSIGNED_BYTE，GL_UNSIGNED_SHORT_5_6_5，GL_UNSIGNED_SHORT_4_4_4_4，和GL_UNSIGNED_SHORT_5_5_5_1。
         * data
         *     指定一个指向内存中图像数据的指针。
         */

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
                null);

        //要开始使用 gpu的fbo，数据区域gpu
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);  //綁定FBO

        //真正发生绑定fbo和纹理(图层)
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, frameTextures[0], 0);

        //释放纹理图层
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        //释放fbo
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    protected void releaseFrame() {
        if (frameTextures != null) {
            GLES20.glDeleteTextures(1, frameTextures, 0);
            frameTextures = null;
        }

        if (frameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, frameBuffer, 0);
        }
    }

    @Override
    public int onDraw(int texture) {
//      数据渲染到fbo中,输出设备就是fbo
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        super.onDraw(texture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        return frameTextures[0] ;
    }


}
