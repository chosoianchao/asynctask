package com.techja.demothread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.techja.demothread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int UPDATE_TV_COUNT = 102;
    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding binding;
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            handleMsg(message);
            return false;
        }
    });
    private int start = 0;
    private Thread th1;

    private void handleMsg(Message message) {
        if (message.what == UPDATE_TV_COUNT) {
            binding.tvCount.setText(String.format("%s", message.arg1));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //c1
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //c2
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
//        binding = ActivityMainBinding.bind(view);
//        setContentView(view);
        initViews();
    }

    private void initViews() {
        //b1 : khởi tạo môi trường Runnable cho Thread mới
        Runnable rb = new Runnable() {
            @Override
            public void run() {
                //b2 Gọi đến các phương thức cần thực thi ở luồng riêng
                startCount();
            }
        };
        binding.btStop.setOnClickListener(view -> {
            if (th1 != null && th1.isAlive()) {
                th1.interrupt();
            }
        });

        binding.btStart.setOnClickListener(view -> {
            if (th1 == null || !th1.isAlive()) {
                th1 = new Thread(rb);
                th1.start();
            }
        });
    }

    private void startCount() {
        for (int i = start; i < 100; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // e.printStackTrace();
                Log.e(TAG, "Thread bị gián đoạn");
                //Buộc hành vi phải dừng lại
                break;
            }
            start++;
            // final  int ii=i;
           /* // đưa câu lệnh update ui vào khối runOnUiThread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.tvCount.setText(String.format("%s" , ii));
                }
            });*/
            Message msg = new Message();
            msg.what = UPDATE_TV_COUNT;
            msg.arg1 = i;
            msg.setTarget(mHandler);
            msg.sendToTarget();
        }
        Log.i(TAG, "Kết thúc hành vi ");
    }


}