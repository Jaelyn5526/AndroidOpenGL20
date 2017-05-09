package jaelyn.myapplication.util;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by zaric on 17-05-04.
 */

public class OpenGLUtile {

    /**
     * 获取shader
     * @param type  GLES20.GL_VERTEX_SHADER， GLES20.GL_FRAGMENT_SHADER
     * @param shaderCode assets文件夹中的文件内容
     * @return
     */
    public static int loadShader(int type, String shaderCode){

        // 创建一个vertex shader类型(GLES20.GL_VERTEX_SHADER)
        // 或fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        if (shader != 0) {
            // 将源码添加到shader并编译之
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

            if (compiled[0] == 0) {
                // shader编译失败，移除Shader
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 将 float[] 转成 FloatBuffer
     * @param data
     * @return
     */
    public static FloatBuffer setFloatBuffer(float[] data){
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

    public static ShortBuffer setShortBuffer(short[] data){
        ShortBuffer sb;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 2);
        bb.order(ByteOrder.nativeOrder());
        sb = bb.asShortBuffer();
        sb.put(data);
        sb.position(0);
        return sb;
    }

}
