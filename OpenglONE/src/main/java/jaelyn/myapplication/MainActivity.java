package jaelyn.myapplication;

import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jaelyn.myapplication.render.BoxColorRender;
import jaelyn.myapplication.render.BoxColorRender2;
import jaelyn.myapplication.render.Image2DRender;
import jaelyn.myapplication.render.Image3DRender;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new Image3DRender(this));
    }
}
