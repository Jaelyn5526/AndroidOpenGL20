package jaelyn.myapplication;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jaelyn.myapplication.render.BoxColorRender;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclelist);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
        private String[] name = new String[]{
                "AngleGLActivity, 基本绘图",
                "AngleGLMatrixActivity, 设定视角",
                "BoxColorRender, 绘制立方体方法1",
                "BoxColorRender2, 绘制立方体方法2",
                "Image2DRender, 2D模型贴图",
                "Image2DRender, 3D模型贴图",
                "Image2DRender, 3D模型贴图"
        };

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder;
            View itemView =  LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.list_item, null, false);
            holder = new Holder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            holder.textView.setText(name[position]);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (position){
                    case 0:
                        intent = new Intent(MainActivity.this, AngleGLActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, AngleGLMatrixActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = OpenGLActivity.setIntent(MainActivity.this, position);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = OpenGLActivity.setIntent(MainActivity.this, position);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = OpenGLActivity.setIntent(MainActivity.this, position);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = OpenGLActivity.setIntent(MainActivity.this, position);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = OpenGLActivity.setIntent(MainActivity.this, position);
                        startActivity(intent);
                        break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return name.length;
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView textView;
            public Holder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }


    }
}
