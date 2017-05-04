# AndroidOpenGL20
Android OpenGL20 的使用


## OpenGL20(一)：opengl的实现 ##
项目OpenglONE，主要是实现了绘制基础图形。

### 搭建环境 ###
android中使用opengl绘制，需要一个view作为容器。可以使用GLSurfaceView、TextureView。在某个小部分显示OpenGLES图形则可以考虑TextureView，一般情况都是使用GLSurfaceView和GLSurfaceView.Renderer，实际的绘图动作都是在GLSurfaceView.Renderer里面发生的。

### Manifest中声明使用OpenGLES ###
为了能使用OpenGLES 2.0 API，你必须在你的manifest中添加以下声明：

<uses-feature android:glEsVersion="0x00020000" android:required="true" />
如果你的应用要使用纹理压缩功能，你必须还要声明设备需要支持什么样的压缩格式：

<supports-gl-texture android:name="GL_OES_compressed_ETC1_RGB8_texture" />
<supports-gl-texture android:name="GL_OES_compressed_paletted_texture" />

### 绘制 ###

一、创建使用容器
```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        //设置使用GL20
        glSurfaceView.setEGLContextClientVersion(2);

        //设置Renderer
        glSurfaceView.setRenderer(new MyGLRender());
        setContentView(glSurfaceView);
    }
```
二、实现Render<br>

新建MyRender实现 GLSurfaceView.Renderer接口，需要实现三个方法：onSurfaceCreated，onSurfaceChanged，onDrawFrame <br>
1.onSurfaceCreated(GL10 gl10, EGLConfig eglConfig)   仅调用一次，用来设置OpenGL的环境。<br>
2.onSurfaceChanged(GL10 gl10, int width, int height)  每次view大小改变时候调用。<br>
3.onDrawFrame(GL10 gl10) 每次view被重绘的时候调用。<br>

三、绘制三角形<br>

OpenGL在手机的坐标如图<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/openglcoord.png)<br>

绘制的流程如下图<br>
![](https://github.com/Jaelyn5526/AndroidOpenGL20/blob/master/image/openglRenderer.png)<br>

#### 1、onSurfaceCreated ####
##### 1.1 & 1.2、createShader, createProgram #####
绘制图像必须有这3个句柄：<br>
a. VerTexShader 用于渲染图形的顶点，图像代码；<br>
b. FragmentShader 用于渲染图像的颜色，纹理；<br>
c. Program 一个OpenGLES对象；<br>

其中VerTexShader，FragmentShader在创建的过程中需要用到着色语言，深入了解可以看这个博客 http://www.tuicool.com/articles/VZVJra<br>
项目中用到着色文件为：vertext_sharder_normal.glsl， fragment_sharder_normal.glsl<br>
vertext_sharder_normal.glsl 文件内容：
```glsl
attribute vec4 vPosition;
void main() {
        gl_Position = vPosition;
}
```

fragment_sharder_normal.glsl 文件内容：
```glsl
precision mediump float;
uniform vec4 vColor;

void main() {
    gl_FragColor = vColor;
}
```
成功创建两个shader之后，就要将shader绑定到program中：
```java
//创建program并绑定shader
private void createProgram(){
            VertexShader = loadShader(GLES20.GL_VERTEX_SHADER, loadGLSL(AngleGLActivity.this, vertexShaderFileName));
            FragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, loadGLSL(AngleGLActivity.this, fragmentShaderFileName));

            // 创建一个空的OpenGL ES Program
            program = GLES20.glCreateProgram();
            // 将vertex shader添加到program
            GLES20.glAttachShader(program, VertexShader);
            // 将fragment shader添加到program
            GLES20.glAttachShader(program, FragmentShader);
            // 创建可执行的 OpenGL ES program
            GLES20.glLinkProgram(program);
        }

//创建shader
public int loadShader(int type, String shaderCode){

            // 创建一个vertex shader类型(GLES20.GL_VERTEX_SHADER)
            // 或fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
            int shader = GLES20.glCreateShader(type);

            // 将源码添加到shader并编译之
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);

            return shader;

        }
```

##### 1.3获取position、Color句柄 #####
```java
// 获取指向vertex shader的成员vPosition的 handle
mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");

// 获取指向fragment shader的成员vColor的handle
mColorHandle = GLES20.glGetUniformLocation(program, "vColor");
```

##### 1.4 处理顶点数据 #####
将顶点数据 float[] 转成 FloatBuffer；


#### 2、onSurfaceChanged ####
##### 2.1 设置渲染框的大小 #####
```java
GLES20.glViewport(0, 0, width, height);
```


#### 3、onDrawFrame ####
##### 3.1 & 3.2 重置背景色、添加Program #####
```java
//重置背景颜色
GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
// 将program加入OpenGL ES环境中
GLES20.glUseProgram(program);
```

##### 3.3 准备顶点 #####
```java
// 启用一个指向三角形的顶点数组的handle
GLES20.glEnableVertexAttribArray(mPositionHandle);
// 准备三角形的坐标数据
GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
```
函数原型：<br>
void glVertexAttribPointer (int index, int size, int type, boolean normalized, int stride, Buffer ptr )<br>
参数含义：<br>
indx: 顶点句柄;
size: 每个顶点的有几个数表示 顶点可以是 2(x, y) 或者 3 (x, y, z);
type: 顶点数组中数据的类型，一般为GL_FLOAT;
normalized: GL_TRUE-访问时，顶点数据被归一化， GL_FALSE-顶点数据直接转换为固定点;
stride: 指连续顶点属性之间的偏移量<br>
        如果为0：它们紧密排列在一起，初始值为0；
        如果normalized为GL_TRUE,意味着整形的值会被映射到区间[-1, 1]有符号 或 [0, 1]无符号
ptr: 顶点缓存数据；

##### 3.5 渲染纹理、颜色 #####
填充颜色
```java
// 设置三角形的颜色
GLES20.glUniform4fv(mColorHandle, 1, color, 0);
```

根据贴图顶点填充颜色
```java
// 启用一个指向三角形的纹理顶点的handle
GLES20.glEnableVertexAttribArray(mColorHandle);
// 准备纹理的坐标数据
GLES20.glVertexAttribPointer(mColorHandle, 2, GLES20.GL_FLOAT, false, 0, coordBuffer);
```

##### 3.7 绘制 #####

绘制元素<br>
```java
 GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length, GLES20.GL_UNSIGNED_SHORT,indexBuffer);
```

整体绘制<br>
```java
 GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, anlgePoint.length / 2);
```
glDrawArrays(int mode, int first, int count)方法详见http://blog.csdn.net/qq_31530015/article/details/52108220<br>
mode的参数可以选择
- GL_POINTS ————绘制独立的点
 - GL_LINE_STRIP————绘制连续的线段,不封闭
 - GL_LINE_LOOP————绘制连续的线段,封闭
 - GL_LINES————顶点两两连接，为多条线段构成
 - GL_TRIANGLES————每隔三个顶点构成一个三角形
 - GL_TRIANGLE_STRIP————每相邻三个顶点组成一个三角形
 - GL_TRIANGLE_FAN————以一个点为三角形公共顶点，组成一系列相邻的三角形

#### 3.8 禁用句柄 ####
```java
 // 禁用指向三角形的顶点数组
 GLES20.glDisableVertexAttribArray(mPositionHandle);
```
