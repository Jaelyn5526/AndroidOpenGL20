## OpenGL20(一)：opengl的视角 ##
在AngleGLActivity中绘制了一个三角形，但是如果旋转屏幕三角形就会变形。只要设置好视角就不会变形了。<br>
AngleGLActivity 竖屏的效果图跟横屏的效果图如下两图：<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/angle_1.png)
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/angle_2.png)<br>

AngleGLMatrixAcitivty横屏的效果图：<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/angle_3.png)<br>
实现这效果是在onDrawFrame中修改视角矩阵：
```java
//指定vMatrix的值
GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, matrix, 0);
```
mMatrixHandle： 是矩阵句柄，与mPositionHandle类似，需要通过 glsl文件获得。<br>
matrix：矩阵值<br>

### 修改glsl文件 ###
将原先的angle_vertex.glsl文件的内容修改为：
```glsl
attribute vec4 vPosition;
uniform mat4 vMatrix;

void main() {
        gl_Position = vMatrix*vPosition;
}
```

### 修改代码 ###
1. 在onSurfaceCreated中加入获取Marix的句柄<br>
```java
// 获取指向vertex shader的成员vMatrix的handle
mMatrixHandle = GLES20.glGetUniformLocation(program, "vMatrix");
```
2. onSurfaceChanged设置矩阵数据
```java
 //计算宽高比
float ratio = (float) width / height;
//设置透视投影
Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
//设置相机位置
Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 3.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//计算变换矩阵
Matrix.multiplyMM(matrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
```
函数：<br>
``java
frustumM(float[] m, int offset, float left, float right, float bottom, float top, float near, float far)
```

m:填充的矩阵，传一个16位的byte数组；
offset: 一般传0；
left，bottom, bottom, top: 单位坐标与对应屏幕的长宽，与GLES20.glViewport(0, 0, width, height)相关。<br>
        如left=-1; right = 1; bottom=-1; top = 1, 则表示：x轴上1个单位的实际像素距离为width/2;y轴上1个单位的实际像素距离为height/2。
near: 摄像头最近的距离;
far: 摄像头最远的距离；
