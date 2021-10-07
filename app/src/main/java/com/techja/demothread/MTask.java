package com.techja.demothread;

import android.os.AsyncTask;

public final class MTask extends AsyncTask<Object, Object, Object> {
    private String key;
    private MTaskListener callBack;

    public MTask(String key, MTaskListener callBack) {
        this.key = key;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        callBack.startExcute();
    }

    public void requestUpdateUI(Object dataUpdate) {
        publishProgress(dataUpdate);
    }

    @Override
    protected Object doInBackground(Object... data) {
        return callBack.excuteStart(data == null ? null : data[0], key, this);
    }

    @Override
    protected void onProgressUpdate(Object... dataUpdate) {
        callBack.updateUI(dataUpdate == null ? null : dataUpdate[0], key);
    }

    @Override
    protected void onPostExecute(Object result) {
        callBack.completeTask(result, key);
    }


    public interface MTaskListener {
        default void startExcute() {
        }

        Object excuteStart(Object dataInput, String key, MTask task);

        default void updateUI(Object dataUpdate, String key) {
            //do nothing
        }

        default void completeTask(Object result, String key) {
            //do nothing
        }
    }
}
