package android.nachiketa.flashlight;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;

public class FlashlightShakeToggle extends Service implements SensorEventListener {

    SensorManager sensorManager = null;
    Vibrator vibrator = null;
    public static final int MIN_TIME_BETWEEN_SHAKES = 1000;
    private long lastShakeTime = 0;
    private boolean isFlashlightOn = false;

    public FlashlightShakeToggle() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Float shakeThreshold;
        try {
            shakeThreshold = Float.parseFloat(AndroidReadWrite.loadFile(getApplicationContext(), "settings.txt"));
        } catch (Exception ex) {
            AndroidReadWrite.saveFile(getApplicationContext(), "settings.txt", 10.2f + "");
            shakeThreshold = 10.2f;
            ex.getMessage();
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastShakeTime) > MIN_TIME_BETWEEN_SHAKES) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

                if (acceleration > shakeThreshold) {
                    lastShakeTime = curTime;
                    if (!isFlashlightOn) {
                        torchToggle("on");
                        isFlashlightOn = true;
                    } else {
                        torchToggle("off");
                        isFlashlightOn = false;
                    }
                }
            }
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
                        vibrator.vibrate(500);
                        isFlashlightOn = true;
                    } else {
                        camManager.setTorchMode(cameraId, false);  // Turn OFF
                        vibrator.vibrate(500);
                        isFlashlightOn = false;
                    }
                }
            } catch (CameraAccessException e) {
                e.getMessage();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Unused Method
    }
}