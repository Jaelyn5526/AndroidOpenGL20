## OpenGL20(四)：纹理 ##
建立好模型后，除了填充颜色还可以将图片绘制在模型的各个面上。<br>

### UV坐标 ###
给一个模型贴纹理时，需要通过某种方式告诉OpenGL用哪一块图像来填充三角形。这是借助UV坐标来实现的。每个顶点除了位置坐标外还有两个浮点数坐标：U和V。这两个坐标用于获取纹理，模型坐标与UV坐标的对比图如下：<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/openglcoordUV.png)<br>
使用一张图片作为纹理则左上，右上，左下， 右下对应的纹理坐标为：（0， 0），（1，0），（0，1）， （1，1）;<br>
纹理的坐标范围为[0, 1], 如果大于1，采用平铺的方式。
详细可看连接：http://blog.chinaunix.net/uid-23392298-id-3387320.html<br>

### 贴2D 纹理 ###
Image2DRender文件中写的是如果贴一张2D图形到一个正方形的平面上。
#### 纹理坐标 ####
```java
  // 贴图纹理
    float texCoor[] = {
            0,0,
            1,0,
            0,1,
            1,1,
    };
    
   // 数组中每个顶点的坐标数
    private float[] boxPoint = new float[]{
            -1f, 1f, 0f, // 0
            1f, 1f, 0f,  //1
            -1f, -1f, 0f,  //2
            1f, -1f, 0f,  //3
    };
```
#### 做色器的使用 ####
做色器代码与颜色填充的有所不同<br>
box_image_fragment.glsl<br>
```glsel
precision mediump float;
uniform sampler2D vTexture;
varying vec2 vColor;

void main() {
    gl_FragColor = texture2D(vTexture, vColor);
}
```

box_image_vertex.glsl<br>
```glsl
attribute vec4 vPosition;
uniform mat4 vMatrix;
varying  vec2 vColor;
attribute vec2 texCoor;

void main() {
    gl_Position = vMatrix*vPosition;
    vColor=texCoor;
}
```

### 设置纹理 ###
在onSurfaceCreated生成、纹理：<br>
```java
public void createTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);//生成一个纹理
        textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        //上面是纹理贴图的取样方式，包括拉伸方式，取临近值和线性值
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);//让图片和纹理关联起来，加载到OpenGl空间中
        bitmap.recycle();//不需要，可以释放
    }

```

### 使用纹理 ###
在onDrawFrame启动该纹理<br>
```java
        // 启用一个指向立方体的顶点数组的handle
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 准备立方体的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // 设置立方体的颜色
        GLES20.glEnableVertexAttribArray(mTexCoorHandle);
        GLES20.glVertexAttribPointer(mTexCoorHandle, 2, GLES20.GL_FLOAT, false, 0, colorBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, boxPoint.length / 3);

```

### 贴立方体纹理 ###
Image2DRender文件中写的是如果贴一张2D图形到一个立方体上。<br>
#### 立方体顶点坐标 ####
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/boxpoint.png)<br>
```java
// 数组中每个顶点的坐标数
    private float[] boxPoint = new float[]{
            -1f, 1f, 1f, // 0
            1f, 1f, 1f,  //1
            1f, -1f, 1f,  //3
            -1f, 1f, 1f, // 0
            -1f, -1f, 1f,  //2
            1f, -1f, 1f,  //3

            -1f, 1f, 1f, // 0
            1f, 1f, 1f,  //1
            1f, 1f, -1f,  //5
            -1f, 1f, 1f, // 0
            -1f, 1f, -1f, //4
            1f, 1f, -1f,  //5

            -1f, 1f, 1f, // 0
            -1f, 1f, -1f, // 4
            -1f, -1f, -1f,  //6
            -1f, 1f, 1f, // 0
            -1f, -1f, 1f,  //2
            -1f, -1f, -1f,  //6

            1f, -1f, -1f,  //7
            1f, 1f, -1f,  //5
            -1f, 1f, -1f, // 4
            1f, -1f, -1f,  //7
            -1f, -1f, -1f,  //6
            -1f, 1f, -1f, // 4

            1f, -1f, -1f,  //7
            1f, 1f, -1f,  //5
            1f, 1f, 1f,  //1
            1f, -1f, -1f,  //7
            1f, -1f, 1f,  //3
            1f, 1f, 1f,  //1

            1f, -1f, -1f,  //7
            -1f, -1f, -1f,  //3
            -1f, -1f, 1f,  //2
            1f, -1f, -1f,  //7
            1f, -1f, 1f,  //6
            -1f, -1f, 1f,  //2

    };
```
#### 6面贴相同的图片 ####
将下图贴到立方体的每个面上，这样跟2D贴图的坐标一样，只要一一对物体顶点坐标就可以<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/OpenglONE/src/main/res/mipmap-xhdpi/circle3.png)<br>
![效果图](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/box_image_1.gif)<br>
```java
//每个面都使用相同的纹理
    float texCoorOne[] = {
            0,0,
            1,0,
            1,1,
            0,0,
            0,1,
            1,1,

            0,0,
            1,0,
            1,1,
            0,0,
            0,1,
            1,1,

            0,0,
            1,0,
            1,1,
            0,0,
            0,1,
            1,1,

            0,0,
            1,0,
            1,1,
            0,0,
            0,1,
            1,1,

            0,0,
            1,0,
            1,1,
            0,0,
            0,1,
            1,1,

            0,0,
            1,0,
            1,1,
            0,0,
            0,1,
            1,1,
    };
 ```

#### 一张图贴满立方体 ####
将下图贴裁剪到立方体的每个面上<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/OpenglONE/src/main/res/mipmap-xhdpi/box.png)<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/box_image_2.gif)<br>
```java
//将一张图 裁剪贴在所有面
    float texCoorTwo[] = {
            0.2f, 0.2f,  //0
            0.4f, 0.2f,  //1
            0.4f, 0.4f,  //3
            0.2f, 0.2f,  //0
            0.2f, 0.4f,  //2
            0.4f, 0.4f,  //3

            0.2f, 0.2f,  //0
            0.4f, 0.2f,  //1
            0.4f, 0f,    //5
            0.2f, 0.2f,  //0
            0.2f, 0f,    //4
            0.4f, 0f,    //5

            0.2f, 0.2f,  //0
            0f, 0.2f,    //4
            0f, 0.4f,    //6
            0.2f, 0.2f,  //0
            0.2f, 0.4f,    //2
            0f, 0.4f,    //6

            0.6f, 0.4f,  //7
            0.6f, 0.2f,  //5
            0.8f, 0.2f,  //4
            0.6f, 0.4f,  //7
            0.8f, 0.4f,  //6
            0.8f, 0.2f,  //4

            0.6f, 0.4f,  //7
            0.6f, 0.2f,  //5
            0.4f, 0.2f,  //1
            0.6f, 0.4f,  //7
            0.4f, 0.4f,  //3
            0.4f, 0.2f,  //1

            0.4f, 0.6f,  //7
            0.2f, 0.6f,  //6
            0.2f, 0.4f,  //2
            0.4f, 0.6f,  //7
            0.4f, 0.4f,  //3
            0.2f, 0.4f,  //2
    };
 ```
























