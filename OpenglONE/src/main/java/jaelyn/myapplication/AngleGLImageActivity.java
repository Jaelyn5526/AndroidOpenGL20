package jaelyn.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制三角形
 * 设定纹理
 */
public class AngleGLImageActivity extends AppCompatActivity {

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


    public class MyGLRender implements GLSurfaceView.Renderer {
        private final String vertexShaderFileName = "angle_matrix_vertex.glsl";
        private final String fragmentShaderFileName = "angle_fragment.glsl";

        private int VertexShader;
        private int FragmentShader;
        private int program;

        // 数组中每个顶点的坐标数
        private float[] anlgePoint = new float[]{
                0, 0.7f,
                -0.6f, -0.6f,
                0.6f, -0.6f
        };

        // 设置颜色，分别为red, green, blue 和alpha (opacity)
        float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

        private FloatBuffer vertexBuffer;
        private int mPositionHandle;
        private int mColorHandle;
        private int mMatrixHandle;

        public float[] mViewMatrix = new float[16];
        public float[] mProjectMatrix = new float[16];
        public float[] matrix = new float[16];

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            //设置背景颜色
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);
            createProgram();

            //处理顶点数据
            vertexBuffer = setFloatBuffer(anlgePoint);


            // 获取指向vertex shader的成员vPosition的 handle
            mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");

            // 获取指向fragment shader的成员vColor的handle
            mColorHandle = GLES20.glGetUniformLocation(program, "vColor");

            // 获取指向vertex shader的成员vMatrix的handle
            mMatrixHandle = GLES20.glGetUniformLocation(program, "vMatrix");
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            //设置渲染窗口的大小
            GLES20.glViewport(0, 0, width, height);

            //计算宽高比
            float ratio = (float) width / height;
            //设置透视投影
            Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
            //设置相机位置
            Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 3.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            //计算变换矩阵
            Matrix.multiplyMM(matrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            //重置背景颜色
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            // 将program加入OpenGL ES环境中
            GLES20.glUseProgram(program);

            //指定vMatrix的值
            GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, matrix, 0);

            // 启用一个指向三角形的顶点数组的handle
            GLES20.glEnableVertexAttribArray(mPositionHandle);

            // 准备三角形的坐标数据
            GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);

            // 设置三角形的颜色
            GLES20.glUniform4fv(mColorHandle, 1, color, 0);

            // 画三角形
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, anlgePoint.length / 2);

            // 禁用指向三角形的顶点数组
            GLES20.glDisableVertexAttribArray(mPositionHandle);

        }

        private void createProgram(){
            VertexShader = loadShader(GLES20.GL_VERTEX_SHADER, loadGLSL(AngleGLImageActivity.this, vertexShaderFileName));
            FragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, loadGLSL(AngleGLImageActivity.this, fragmentShaderFileName));

            // 创建一个空的OpenGL ES Program
            program = GLES20.glCreateProgram();
            // 将vertex shader添加到program
            GLES20.glAttachShader(program, VertexShader);
            // 将fragment shader添加到program
            GLES20.glAttachShader(program, FragmentShader);
            // 创建可执行的 OpenGL ES program
            GLES20.glLinkProgram(program);
        }

        public int loadShader(int type, String shaderCode){

            // 创建一个vertex shader类型(GLES20.GL_VERTEX_SHADER)
            // 或fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
            int shader = GLES20.glCreateShader(type);

            // 将源码添加到shader并编译之
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);

            return shader;

        }

        /**
         * 用于读取assets中的文件
         * @param context
         * @param fileName
         * @return
         */
        public String loadGLSL(Context context, String fileName){
            try{
                InputStream is = context.getResources().getAssets().open(fileName);
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader bs = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bs.readLine()) != null){
                    sb.append(line);
                }
                return sb.toString();
            }catch (IOException e){
                e.printStackTrace();

            }
            return null;
        }

        public FloatBuffer setFloatBuffer(float[] data){
            FloatBuffer fb;
            // 为存放形状的坐标，初始化顶点字节缓冲
            ByteBuffer bb = ByteBuffer.allocateDirect(
                    // (坐标数 * 4)float占四字节
                    data.length * 4);
            // 设用设备的本点字节序
            bb.order(ByteOrder.nativeOrder());

            // 从ByteBuffer创建一个浮点缓冲
            fb = bb.asFloatBuffer();
            // 把坐标们加入FloatBuffer中
            fb.put(data);
            // 设置buffer，从第一个坐标开始读
            fb.position(0);
            return fb;
        }

    }
}
