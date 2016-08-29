package com.sample.textapisample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;

    private TextView mTextView;

    private CameraSource mCameraSource;

    private OcrTextView mOcrTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textview);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        textRecognizer.setProcessor(new TextDetectorProcessor());
        CameraSource.Builder cameraSourceBuilder = new CameraSource.Builder(this, textRecognizer);
        cameraSourceBuilder.setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedFps(10f)
                .setAutoFocusEnabled(true);
        mCameraSource = cameraSourceBuilder.build();

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mSurfaceView.getHolder().addCallback(mCallback);

        mOcrTextView = (OcrTextView) findViewById(R.id.ocr_textview);
    }

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d("Debug", "does not have permission");
                    return;
                }
                mCameraSource.start(mSurfaceView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    private class TextDetectorProcessor implements Detector.Processor<TextBlock>{

        @Override
        public void release() {

        }

        @Override
        public void receiveDetections(final Detector.Detections<TextBlock> detections) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SparseArray<TextBlock> items = detections.getDetectedItems();
                    String text = "";

                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock item = items.valueAt(i);
                        text += item.getValue() + "\n";
                    }
                    mTextView.setText(text);

                    mOcrTextView.showFrame(items);
                }
            });
        }
    }
}
