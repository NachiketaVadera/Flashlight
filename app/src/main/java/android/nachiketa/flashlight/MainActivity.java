package android.nachiketa.flashlight;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Global global;
    boolean isTorchOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        global = new Global();

        startService(new Intent(this, ShakeDetectService.class));
    }

    public void toggle(View view) {
        Button button = (Button) view;
        if (button.getText().equals("Switch On")) {
            button.setText(R.string.switch_off_text);
            torchToggle("on");
        } else {
            button.setText(R.string.switch_on_text);
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
                        isTorchOn = true;
                    } else {
                        camManager.setTorchMode(cameraId, false);  // Turn OFF
                        isTorchOn = false;
                    }
                }
            } catch (CameraAccessException e) {
                e.getMessage();
            }
        }
    }

    public void goToSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
