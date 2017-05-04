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
二、实现Render
新建MyRender实现 GLSurfaceView.Renderer接口，需要实现三个方法：onSurfaceCreated，onSurfaceChanged，onDrawFrame
1.onSurfaceCreated(GL10 gl10, EGLConfig eglConfig)   仅调用一次，用来设置OpenGL的环境。
2.onSurfaceChanged(GL10 gl10, int width, int height)  每次view大小改变时候调用。
3.onDrawFrame(GL10 gl10) 每次view被重绘的时候调用。
