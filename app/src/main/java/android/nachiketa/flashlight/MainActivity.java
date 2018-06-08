package android.nachiketa.flashlight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeLayout = null;
    boolean isFlashlightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.relLayMain);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, FlashlightShakeToggle.class));
        Log.i("Flashlight_onStart", "Starting service");
    }

    public void toggle(View view) {
        Button button = (Button) view;
        if (button.getText().equals("Switch On")) {
            relativeLayout.setBackgroundColor(Color.WHITE);
            button.setText("Switch Off");
            torchToggle("on");
        } else {
            button.setText("Switch On");
            relativeLayout.setBackgroundColor(Color.BLACK);
            torchToggle("off");
        }
    }

    private void torchToggle(String command) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; // Usually back camera is at 0 position.
            try {
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0];
                }
                if (camManager != null) {
                    if (command.equals("on")) {
                        camManager.setTorchMode(cameraId, true);   // Turn ON
                    } else {
                        camManager.setTorchMode(cameraId, false);  // Turn OFF
                    }
                }
            } catch (CameraAccessException e) {
                e.getMessage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, FlashlightShakeToggle.class));
    }

    public void goToSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
