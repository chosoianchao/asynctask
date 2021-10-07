package com.techja.demothread;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techja.demothread.databinding.SecondMainBinding;

public class SecondActivity extends AppCompatActivity implements MTask.MTaskListener {
    private static final String TAG = SecondActivity.class.getName();
    private static final String KEY_TASK_COUTING = "KEY_TASK_COUTING";
    private static final String KEY_TASK_DOWNLOAD = "KEY_TASK_DOWNLOAD";
    private MTask task;
    private SecondMainBinding binding;


    private void updateCounting(int count) {
        binding.tvCount.setText(String.format("%s", count));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //c1
        binding = SecondMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //c2
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
//        binding = ActivityMainBinding.bind(view);
//        setContentView(view);
        initViews();
    }

    private void initViews() {
        binding.btStop.setOnClickListener(view -> SecondActivity.this.stopAsyncTask());
        binding.btStart.setOnClickListener(view -> SecondActivity.this.startAsyncTask());
    }

    private void startAsyncTask() {
        task = new MTask(KEY_TASK_COUTING, this);
        task.execute(20);

    }

    private void startDowload() {
        task = new MTask(KEY_TASK_DOWNLOAD, this);
        task.execute(20);

    }

    private void stopAsyncTask() {
        if (task == null) return;
        task.cancel(true);
        task = null;
    }

    @Override
    public void startExcute() {
        binding.tvCount.setText("0");
    }

    @Override
    public Object excuteStart(Object dataInput, String key, MTask task) {
        if (key.equals(KEY_TASK_COUTING)) {
            return doCounting(dataInput, task);
        }
        return doDownload(dataInput, task);
    }

    private Object doDownload(Object dataInput, MTask task) {
        return null;
    }


    @Override
    public void updateUI(Object dataUpdate, String key) {
        if (key.equals(KEY_TASK_COUTING)) {
            updateCounting((int) dataUpdate);
        } else if (key.equals(KEY_TASK_DOWNLOAD)) {
            updateDownloadUI(dataUpdate);
        }
    }

    private void updateDownloadUI(Object dataUpdate) {
    }

    @Override
    public void completeTask(Object result, String key) {
        if (key.equals(KEY_TASK_COUTING)) {
            Toast.makeText(SecondActivity.this, (boolean) result ? "Success" : "Failed", Toast.LENGTH_LONG).show();
        } else if (key.equals(KEY_TASK_DOWNLOAD)) {
            openFile(result);
        }

    }

    private void openFile(Object result) {
    }


    private Object doCounting(Object dataInput, MTask task) {
        for (int i = 0; i < (int) dataInput; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // e.printStackTrace();
                Log.e(TAG, "Thread bị gián đoạn");
                //Buộc hành vi phải dừng lại
                return false;
            }
            //Truyền giá trị i để cập nhật Ui ở onProgressUpdate
            task.requestUpdateUI(i);
            //start++;
        }
        return true;
    }
}
