#extension GL_OES_EGL_image_external : require
//必须 写的 固定的  意思   用采样器
//所有float类型数据的精度是lowp
precision mediump float;
varying vec2 aCoord;
//采样器  uniform static
uniform samplerExternalOES vTexture;
void main(){
//Opengl 自带函数
    vec4 rgba = texture2D(vTexture,aCoord);
//    灰色  滤镜
    float color=(rgba.r + rgba.g + rgba.b) / 3.0;
    vec4 tempColor=vec4(color,color,color,1);
    gl_FragColor=tempColor;

}