
//当前上色点的坐标
varying highp vec2 aCoord;

uniform sampler2D vTexture;

//放大系数 scalePercent > 1 并且不断放大
uniform highp float scalePercent;
//混合透明度 由大变小
uniform lowp float mixturePercent;

void main(){
    //颜色[r、g、b、a]
//    lowp vec4 rgba = texture2D(vTexture, aCoord);

    //特效在这里做

    //中心点
    highp vec2 center = vec2(0.5, 0.5);

    highp vec2 textureCoordinateToUse = aCoord;

    textureCoordinateToUse -= center;

    //采样点一定比需要渲染的坐标点要小
    textureCoordinateToUse = textureCoordinateToUse / scalePercent;

    textureCoordinateToUse += center;

    lowp vec4 textureColor = texture2D(vTexture, aCoord);

    lowp vec4 textureColor2 = texture2D(vTexture, textureCoordinateToUse);

    gl_FragColor = mix(textureColor, textureColor2, mixturePercent);

}