package jaelyn.myapplication.util;

import android.opengl.GLES20;

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
}
