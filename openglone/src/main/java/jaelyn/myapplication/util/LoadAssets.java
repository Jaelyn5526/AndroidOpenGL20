package jaelyn.myapplication.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zaric on 17-05-04.
 */

public class LoadAssets {

    /**
     * 用于读取assets中的文件
     * @param context
     * @param fileName
     * @return
     */
    public static String loadGLSL(Context context, String fileName){
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
}
