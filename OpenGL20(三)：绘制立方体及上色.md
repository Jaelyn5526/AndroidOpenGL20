## OpenGL20(三)：绘制立方体及上色 ##
BoxColorRender & BoxColorRender2分别用两种方法实现了绘制立方体。<br>
立方体的坐标如下图：<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/boxpoint.png)

### 第一种坐标方法 ###
使用的绘制方法为：<br>
```java
GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, boxPoint.length / 3);<br>
```
与前两个示例一样，依次罗列立方体的各个顶点。将立方体的面分割为三角形，将组成三角形的各个顶点一次罗列。<br>
根据上图的，将立方体分为三角形如果：0，1，3 组成一个三角形，这样就得到了立方体的坐标参数：<br>
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
            1f, 1f, -1f,  //7
            -1f, 1f, 1f, // 0
            -1f, 1f, -1f, //4
            1f, 1f, -1f,  //7

            -1f, 1f, 1f, // 0
            -1f, 1f, -1f, // 4
            -1f, -1f, -1f,  //5
            -1f, 1f, 1f, // 0
            -1f, -1f, 1f,  //2
            -1f, -1f, -1f,  //5

            1f, -1f, -1f,  //6
            1f, 1f, -1f,  //7
            -1f, 1f, -1f, // 4
            1f, -1f, -1f,  //6
            -1f, -1f, -1f,  //5
            -1f, 1f, -1f, // 4

            1f, -1f, -1f,  //6
            1f, 1f, -1f,  //7
            1f, 1f, 1f,  //1
            1f, -1f, -1f,  //6
            1f, -1f, 1f,  //3
            1f, 1f, 1f,  //1

            1f, -1f, -1f,  //6
            1f, -1f, 1f,  //3
            -1f, -1f, 1f,  //2
            1f, -1f, -1f,  //6
            -1f, -1f, -1f,  //5
            -1f, -1f, 1f,  //2
    };
```

  2.设置顶点颜色<br>
  给每个坐标顶点都写入一个颜色值<br>
  ```java
   // 设置颜色，分别为red, green, blue 和alpha (opacity)
    float color[] = {
            0.0f,1.0f,0.0f,1.0f,
            0.0f,1.0f,0.0f,1.0f,
            0.0f,1.0f,0.0f,1.0f,
            0.0f,0.5f,0.0f,1.0f,
            0.0f,0.5f,0.0f,1.0f,
            0.0f,0.5f,0.0f,1.0f,

            0.0f,1.0f,1.0f,1.0f,
            0.0f,1.0f,1.0f,1.0f,
            0.0f,1.0f,1.0f,1.0f,
            0.0f,1.0f,0.5f,1.0f,
            0.0f,1.0f,0.5f,1.0f,
            0.0f,1.0f,0.5f,1.0f,

            0.5f,1.0f,0.0f,1.0f,
            0.5f,1.0f,0.0f,1.0f,
            0.5f,1.0f,0.0f,1.0f,
            0.0f,1.0f,0.3f,1.0f,
            0.0f,1.0f,0.3f,1.0f,
            0.0f,1.0f,0.3f,1.0f,

            1.0f,0.0f,0.3f,1.0f,
            1.0f,0.0f,0.3f,1.0f,
            1.0f,0.0f,0.3f,1.0f,
            1.0f,0.6f,0.0f,1.0f,
            1.0f,0.6f,0.0f,1.0f,
            1.0f,0.6f,0.0f,1.0f,

            1.2f,0.0f,0.0f,1.0f,
            1.2f,0.0f,0.0f,1.0f,
            1.2f,0.0f,0.0f,1.0f,
            1.0f,0.7f,0.0f,1.0f,
            1.0f,0.7f,0.0f,1.0f,
            1.0f,0.7f,0.0f,1.0f,

            1.0f,0.0f,1.0f,1.0f,
            1.0f,0.0f,1.0f,1.0f,
            1.0f,0.0f,1.0f,1.0f,
            1.0f,0.3f,0.6f,1.0f,
            1.0f,0.3f,0.6f,1.0f,
            1.0f,0.3f,0.6f,1.0f,
    };
  ```
### 第二种坐标方法 ###
 使用的绘制方法为：<br>
 ```java
 GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);<br>
 ```
 1.生成坐标
 第二种方法，只需要立方体的8个顶点坐标，以及一组用来标注顺序的坐标。<br>
 顶点坐标：<br>
 ```java
     // 数组中每个顶点的坐标数
    private float[] boxPoint = new float[]{
            -1f,  1f,  1f, // 0
             1f,  1f,  1f,  //1
            -1f, -1f,  1f,  //2
             1f, -1f,  1f,  //3
            -1f,  1f, -1f, // 4
             1f,  1f, -1f,  //5
            -1f, -1f, -1f,  //6
             1f, -1f, -1f,  //7
    };
 ```
 顺序数组：<br>
 ```java
     public final short index[]={
            0,1,3,0,2,3,
            0,4,5,0,1,5,
            0,2,6,0,4,6,
            7,5,1,7,3,1,
            7,6,4,7,5,4,
            7,3,2,7,6,2,

    };
```

  2.设置顶点颜色<br>
  只需要给立方体的顶点设置颜色值就可以了<br>
  ```java
     // 设置颜色，分别为red, green, blue 和alpha (opacity)
    float color[] = {
            0.0f,1.0f,0.0f,1.0f,
            0.0f,1.0f,0.0f,1.0f,
            0.0f,1.0f,0.0f,1.0f,
            0.0f,1.0f,0.0f,1.0f,
            1.0f,0.5f,0.0f,1.0f,
            1.0f,0.5f,0.0f,1.0f,
            1.0f,0.5f,0.0f,1.0f,
            1.0f,0.5f,0.0f,1.0f,
    };
```

 
 ### 容易有BUG ###
 1.画3D图型，坐标点为3个点。在使用GLES20.glDrawArrays函数时，第三个参数为顶点数 为：boxPoint.length / 3。<br>
   2D图型，坐标点为2个点，传入的参数为：boxPoint.length / 2。<br>
```java
GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, boxPoint.length / 3);<br>
```
 2.使用的着色器代码，需要将用户设置的颜色传入到顶点着色器：<br>
 ```glse
attribute vec4 vPosition;
uniform mat4 vMatrix;
varying  vec4 vColor;
attribute vec4 aColor;

void main() {
    gl_Position = vMatrix*vPosition;
    vColor=aColor;
}
```
