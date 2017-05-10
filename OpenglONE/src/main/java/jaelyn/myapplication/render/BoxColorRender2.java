package jaelyn.myapplication.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jaelyn.myapplication.util.LoadAssets;
import jaelyn.myapplication.util.MatrixUtil;
import jaelyn.myapplication.util.OpenGLUtile;

/**
 * Created by zaric on 17-05-05.
 */

public class BoxColorRender2 implements GLSurfaceView.Renderer {
    private final String vertexShaderFileName = "box_color_vertex.glsl";
    private final String fragmentShaderFileName = "box_color_fragment.glsl";

    private int VertexShader;
    private int FragmentShader;
    private int program;

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

    public final short index[]={
            0,1,3,0,2,3,
            0,4,5,0,1,5,
            0,2,6,0,4,6,
            7,5,1,7,3,1,
            7,6,4,7,5,4,
            7,3,2,7,6,2,

    };

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

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMatrixHandle;

    private Context context;

    MatrixUtil matrixUtil;

    public BoxColorRender2(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //设置背景颜色
        GLES20.glClearColor(0.1f, 0.5f, 0.5f, 1);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        createProgram();

        //处理顶点数据
        vertexBuffer = OpenGLUtile.setFloatBuffer(boxPoint);
        colorBuffer = OpenGLUtile.setFloatBuffer(color);
        indexBuffer = OpenGLUtile.setShortBuffer(index);

        // 获取指向vertex shader的成员vPosition的 handle
        mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // 获取指向fragment shader的成员aColor的handle
        mColorHandle = GLES20.glGetAttribLocation(program, "aColor");

        // 获取指向vertex shader的成员vMatrix的handle
        mMatrixHandle = GLES20.glGetUniformLocation(program, "vMatrix");

        matrixUtil = new MatrixUtil();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //设置渲染窗口的大小
        GLES20.glViewport(0, 0, width, height);

        //计算宽高比
        float ratio = (float) width / height;

        //设置透视投影
        matrixUtil.frustum(-ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        matrixUtil.setCamera(5f, 5f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        matrixUtil.getFinalMatrix();

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //重置背景颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT  | GLES20.GL_DEPTH_BUFFER_BIT);

        // 将program加入OpenGL ES环境中
        GLES20.glUseProgram(program);

        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, matrixUtil.autoRotate(), 0);

        // 启用一个指向立方体的顶点数组的handle
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 准备立方体的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // 设置立方体的颜色
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // 禁用指向立方体的顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    private void createProgram(){
        VertexShader = OpenGLUtile.loadShader(GLES20.GL_VERTEX_SHADER, LoadAssets.loadGLSL(context, vertexShaderFileName));
        FragmentShader = OpenGLUtile.loadShader(GLES20.GL_FRAGMENT_SHADER, LoadAssets.loadGLSL(context, fragmentShaderFileName));

        // 创建一个空的OpenGL ES Program
        program = GLES20.glCreateProgram();
        // 将vertex shader添加到program
        GLES20.glAttachShader(program, VertexShader);
        // 将fragment shader添加到program
        GLES20.glAttachShader(program, FragmentShader);
        // 创建可执行的 OpenGL ES program
        GLES20.glLinkProgram(program);
    }


}
