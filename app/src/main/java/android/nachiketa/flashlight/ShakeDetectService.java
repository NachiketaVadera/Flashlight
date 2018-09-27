package android.nachiketa.flashlight;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

public class ShakeDetectService extends Service implements SensorEventListener {

    public static final int MIN_TIME_BETWEEN_SHAKES = 1000;
    SensorManager sensorManager = null;
    Vibrator vibrator = null;
    private long lastShakeTime = 0;
    private boolean isFlashlightOn = false;
    private Float shakeThreshold;

    public ShakeDetectService() {
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

        try {
            shakeThreshold = Float.parseFloat(Global.loadFile(getApplicationContext(), "settings.txt"));
        } catch (Exception ex) {
            Global.saveFile(getApplicationContext(), "settings.txt", String.valueOf(10.2f));
            shakeThreshold = 10.2f;
            ex.getMessage();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Global global = new Global();

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
                        isFlashlightOn = global.torchToggle("on", this);
                    } else {
                        isFlashlightOn = global.torchToggle("off", this);
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Unused Method
    }
}
