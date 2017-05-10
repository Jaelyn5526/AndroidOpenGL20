package jaelyn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jaelyn.myapplication.render.BoxColorRender;
import jaelyn.myapplication.render.BoxColorRender2;
import jaelyn.myapplication.render.Image2DRender;
import jaelyn.myapplication.render.Image3DRender;

public class OpenGLActivity extends AppCompatActivity {

    public static Intent setIntent(Context context, int position){
        Intent intent = new Intent(context, OpenGLActivity.class);
        intent.putExtra("position", position);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl);
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
        glSurfaceView.setEGLContextClientVersion(2);
//        glSurfaceView.setRenderer(new Image3DRender(this));
        Intent intent = getIntent();
        switch (intent.getIntExtra("position", 2)){
        case 2:
            glSurfaceView.setRenderer(new BoxColorRender(this));
            break;
        case 3:
            glSurfaceView.setRenderer(new BoxColorRender2(this));
            break;
        case 4:
            glSurfaceView.setRenderer(new Image2DRender(this));
            break;
        case 5:
            glSurfaceView.setRenderer(new Image3DRender(this, 0));
            break;
        case 6:
            glSurfaceView.setRenderer(new Image3DRender(this, 1));
            break;
        }
    }
}
