package first.winning.com.measuresizedemo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView tvWidth;
    private TextView tvHeight;

    @SuppressLint("HandlerLeak")
   private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tvWidth.setText(btn.getWidth()+"");
                    tvHeight.setText(btn.getHeight()+"");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnMeasure);
        tvWidth = findViewById(R.id.tvWidth);
        tvHeight = findViewById(R.id.tvHeight);

        //方法1.通过onWindowFocusChange方法来判断，就是判断window是否初始化完成
        //方法2.获取ViewTreeObserver,添加addOnGlobalLayoutListener事件,逻辑上是在onLayout事件之后进行
        measuerSize1();
        //方法3，也是先获取ViewTreeObserver,添加OnPreDrawListener事件之前
        measureSize2();
        //方法4.通过view的post()异步方法来获取到控件的宽高
        measureSize3();
        //方法5.获取控件宽高放在放在handler中去处理
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            tvWidth.setText(btn.getWidth()+"");
            tvHeight.setText(btn.getHeight()+"");
        }
    }


    public void measuerSize1(){
        ViewTreeObserver viewTreeObserver = this.getWindow().getDecorView().getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvWidth.setText(btn.getMeasuredWidth()+"");
                tvHeight.setText(btn.getMeasuredHeight()+"");
                //获取到宽高之后就可以把事件给销毁掉
                MainActivity.this.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    public void measureSize2(){
        ViewTreeObserver viewTreeObserver = this.getWindow().getDecorView().getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tvWidth.setText(btn.getMeasuredWidth()+"");
                tvHeight.setText(btn.getMeasuredHeight()+"");
                //获取到宽高之后就可以把事件给销毁掉
                MainActivity.this.getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    public void measureSize3(){
        btn.post(new Runnable() {
            @Override
            public void run() {
                tvWidth.setText(btn.getWidth()+"");
                tvHeight.setText(btn.getHeight()+"");
            }
        });
    }
}

