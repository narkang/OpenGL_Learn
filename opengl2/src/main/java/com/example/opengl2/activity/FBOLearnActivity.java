package com.example.opengl2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.opengl2.R;
import com.example.opengl2.fbo.CameraFboView;
import com.example.opengl2.widget.RecordButton;

public class FBOLearnActivity extends AppCompatActivity implements RecordButton.OnRecordListener, RadioGroup.OnCheckedChangeListener {

    private CameraFboView cameraView;
    private RecordButton btn_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_b_o_learn);

        cameraView = findViewById(R.id.cameraView);
        btn_record = findViewById(R.id.btn_record);
        btn_record.setOnRecordListener(this);

        //速度
        RadioGroup rgSpeed = findViewById(R.id.rg_speed);
        rgSpeed.setOnCheckedChangeListener(this);

        checkPermission();
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, 1);

        }
        return false;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_extra_slow:
                cameraView.setSpeed(CameraFboView.Speed.MODE_EXTRA_SLOW);
                break;
            case R.id.btn_slow:
                cameraView.setSpeed(CameraFboView.Speed.MODE_SLOW);
                break;
            case R.id.btn_normal:
                cameraView.setSpeed(CameraFboView.Speed.MODE_NORMAL);
                break;
            case R.id.btn_fast:
                cameraView.setSpeed(CameraFboView.Speed.MODE_FAST);
                break;
            case R.id.btn_extra_fast:
                cameraView.setSpeed(CameraFboView.Speed.MODE_EXTRA_FAST);
                break;
        }
    }

    @Override
    public void onRecordStart() {
        cameraView.startRecord();
        btn_record.setText("拍摄中");
    }

    @Override
    public void onRecordStop() {
        cameraView.stopRecord();
        btn_record.setText("点击拍摄");
    }
}