precision mediump float; // 数据精度

//当前上色点的坐标
varying highp vec2 aCoord;

uniform sampler2D vTexture;

void main(){

    float y = aCoord.y;

//    二分屏
//    if(y<0.5){
//
//        y += 0.25;
//
//    }else {
//
//        y -= 0.25;
//
//    }

//    三分屏
    float a = 1.0 / 3.0;

    if(y < a){
        y += a;
    }else if(y > 2.0 * a){
        y -= a;
    }

    gl_FragColor = texture2D(vTexture, vec2(aCoord.x, y));

}