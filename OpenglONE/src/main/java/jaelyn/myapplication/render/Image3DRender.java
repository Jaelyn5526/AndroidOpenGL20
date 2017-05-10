package jaelyn.myapplication.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jaelyn.myapplication.R;
import jaelyn.myapplication.util.LoadAssets;
import jaelyn.myapplication.util.MatrixUtil;
import jaelyn.myapplication.util.OpenGLUtile;

/**
 * Created by zaric on 17-05-05.
 * 实现贴图，将立方体的各个面贴上同一个纹理图。
 */

public class Image3DRender extends BaseRender {
    private final String vertexShaderFileName = "box_image_vertex.glsl";
    private final String fragmentShaderFileName = "box_image_fragment.glsl";

    private int VertexShader;
    private int FragmentShader;
    private int program;
    private Bitmap bitmap;

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

    //将一张图面所有面
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

    float[] texCoor;
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;

    private int mPositionHandle;
    private int mTexCoorHandle;
    private int mMatrixHandle;

    private Context context;

    MatrixUtil matrixUtil;
    private int textureId;

    public Image3DRender(Context context) {
        this.context = context;

        /*this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.circle3);
        texCoor = texCoorOne;*/

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.box);
        texCoor = texCoorTwo;
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
        colorBuffer = OpenGLUtile.setFloatBuffer(texCoor);

        // 获取指向vertex shader的成员vPosition的 handle
        mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // 获取指向fragment shader的成员texCoor的handle
        mTexCoorHandle = GLES20.glGetAttribLocation(program, "texCoor");

        // 获取指向vertex shader的成员vMatrix的handle
        mMatrixHandle = GLES20.glGetUniformLocation(program, "vMatrix");

        matrixUtil = new MatrixUtil();

        createTexture();
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
        matrixUtil.setCamera(5f, -5f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        matrixUtil.getFinalMatrix();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //重置背景颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // 将program加入OpenGL ES环境中
        GLES20.glUseProgram(program);

        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, matrixUtil.autoRotate(), 0);
//        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, matrixUtil.getMatrix(), 0);

        // 启用一个指向立方体的顶点数组的handle
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 准备立方体的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // 设置立方体的颜色
        GLES20.glEnableVertexAttribArray(mTexCoorHandle);
        GLES20.glVertexAttribPointer(mTexCoorHandle, 2, GLES20.GL_FLOAT, false, 0, colorBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, boxPoint.length / 3);

        // 禁用指向立方体的顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private void createProgram() {
        VertexShader = OpenGLUtile.loadShader(GLES20.GL_VERTEX_SHADER,
                LoadAssets.loadGLSL(context, vertexShaderFileName));
        FragmentShader = OpenGLUtile.loadShader(GLES20.GL_FRAGMENT_SHADER,
                LoadAssets.loadGLSL(context, fragmentShaderFileName));

        // 创建一个空的OpenGL ES Program
        program = GLES20.glCreateProgram();
        // 将vertex shader添加到program
        GLES20.glAttachShader(program, VertexShader);
        // 将fragment shader添加到program
        GLES20.glAttachShader(program, FragmentShader);
        // 创建可执行的 OpenGL ES program
        GLES20.glLinkProgram(program);
    }

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
}
