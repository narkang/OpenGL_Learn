#extension GL_OES_EGL_image_external : require
//必须 写的 固定的  意思   用采样器
//所有float类型数据的精度是lowp
precision mediump float;
varying vec2 aCoord;
//采样器  uniform static
uniform samplerExternalOES vTexture;
void main(){
//Opengl 自带函数
    //逆时针旋转90度
//    vec4 rgba = texture2D(vTexture,vec2(1.0-aCoord.y,aCoord.x));
    //顺时针旋转90度
//    vec4 rgba = texture2D(vTexture, vec2(aCoord.y, 1.0-aCoord.x));

    //顺时针旋转180°
        vec4 rgba = texture2D(vTexture, vec2(aCoord.y, aCoord.x));
    gl_FragColor=vec4(rgba.r,rgba.g,rgba.b,rgba.a);



}