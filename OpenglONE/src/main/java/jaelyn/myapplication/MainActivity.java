package jaelyn.myapplication;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jaelyn.myapplication.render.BoxColorRender;
import jaelyn.myapplication.render.BoxColorRender2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new BoxColorRender2(this));
    }
}
