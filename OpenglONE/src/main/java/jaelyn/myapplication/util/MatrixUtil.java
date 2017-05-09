package jaelyn.myapplication.util;

import android.opengl.Matrix;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by zaric on 17-05-05.
 */

public class MatrixUtil {

    private float[] mMatrix=new float[16];    // 当前矩阵
    private float[] mMatrixCamera=new float[16];    //相机矩阵
    private float[] mMatrixProjection=new float[16];    //投影矩阵
    public float[] mMatrixCurrent=     //原始矩阵
            {1,0,0,0,
                    0,1,0,0,
                    0,0,1,0,
                    0,0,0,1};

    private Stack<float[]> mStack;      //变换矩阵堆栈

    public MatrixUtil(){
        mStack=new Stack<>();
    }

    //保护现场
    public void pushMatrix(){
        mStack.push(Arrays.copyOf(mMatrixCurrent,16));
    }

    //恢复现场
    public void popMatrix(){
        mMatrixCurrent=mStack.pop();
    }

    public void clearStack(){
        mStack.clear();
    }

    //平移变换
    public void translate(float x,float y,float z){
        Matrix.translateM(mMatrixCurrent,0,x,y,z);
    }

    //旋转变换
    public void rotate(float angle,float x,float y,float z){
        Matrix.rotateM(mMatrixCurrent,0,angle,x,y,z);
    }

    //缩放变换
    public void scale(float x,float y,float z){
        Matrix.scaleM(mMatrixCurrent,0,x,y,z);
    }

    //设置相机
    public void setCamera(float ex,float ey,float ez,float cx,float cy,float cz,float ux,float uy,float uz){
        Matrix.setLookAtM(mMatrixCamera,0,ex,ey,ez,cx,cy,cz,ux,uy,uz);
    }

    public void frustum(float left,float right,float bottom,float top,float near,float far){
        Matrix.frustumM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    public void ortho(float left,float right,float bottom,float top,float near,float far){
        Matrix.orthoM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    /**
     * 计算矩阵
     * @return
     */
    public float[] getFinalMatrix(){
        mMatrix=new float[16];
        Matrix.multiplyMM(mMatrix,0,mMatrixCamera,0,mMatrixCurrent,0);
        Matrix.multiplyMM(mMatrix,0,mMatrixProjection,0,mMatrix,0);
        return mMatrix;
    }

    public float[] getMatrix(){
        return mMatrix;
    }

    float degree1 = 0;
    float degree2 = 0;

    public float[] autoRotate(){
        float[] tmpMatrix = new float[16];
        System.arraycopy(mMatrix, 0, tmpMatrix, 0, 16);
        float x, y, z;
        x = (float) Math.sin(degree1 * Math.PI / 180);
        y = 2;
        z = (float) Math.cos(degree1 * Math.PI / 180);
        degree1 += 0.2;
        Matrix.rotateM(tmpMatrix, 0, degree2, x, y, z);
        degree2 += 1;
        return tmpMatrix;
    }
}
