# Flashlight

![Project Logo](https://raw.githubusercontent.com/NachiketaVadera/Flashlight/master/images/gif/gif1.gif)


[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=102)](https://github.com/NachiketaVadera/Flashlight)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Summary

The base of this project is LED provided along with the Camera in a typical Android smart phone. After seeing that Play Store has a number of apps that ask for unnecessary permissions, I decided to create an app that toggles the LED without asking for any type of permission. This app also implements a service that detects when the device is shook (used to toggle the flashlight). 

As of June 24th of 2018, Flashlight is licensed under Apache License version 2.0.

## Downloads

Latest apk:
[Flashlight.apk](https://github.com/NachiketaVadera/Flashlight/releases/download/v1.5/Flashlight-v1.5.apk)

Source Code (zip):
[Flashlight.zip](https://github.com/NachiketaVadera/Flashlight/archive/v1.5.zip)

Source Code (tar.gz):
[Flashlight.tar.gz](https://github.com/NachiketaVadera/Flashlight/archive/v1.5.tar.gz)

## Contributors

* Code Optimization : [RamusisTrenksmas](https://github.com/RamusisTrenksmas)
* Logo and Images : [Dee-y](https://github.com/dee-y)

## Code

The majority of the code is written in Java and is simple. For detecting shake motion:
Declare global variables:

```java
    SensorManager sensorManager = null;
    public static final float SHAKE_THRESHOLD = 10.25f; // How hard should user shake to invoke the service
    public static final int MIN_TIME_BETWEEN_SHAKES = 1000;
    private long lastShakeTime = 0;
    private boolean isFlashlightOn = false;
```

Initialize sensorManager:

```java
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    if (sensorManager != null) {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
```

Make class implement SensorEventListener interface and override it's onSensorChanged method:

```java
      @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastShakeTime) > MIN_TIME_BETWEEN_SHAKES) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

                if (acceleration > SHAKE_THRESHOLD) {
                    lastShakeTime = curTime;
                    if (!isFlashlightOn) {
                        torchToggle("on");
                        isFlashlightOn = true;
                    }
                    else {
                        torchToggle("off");
                        isFlashlightOn = false;
                    }
                }
            }
        }
```

Create a function torchToggle(String command):

```java
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
                        isFlashlightOn = true;
                    } else {
                        camManager.setTorchMode(cameraId, false);  // Turn OFF
                        isFlashlightOn = false;
                    }
                }
            } catch (CameraAccessException e) {
                e.getMessage();
            }
        }
    }
```

## Contributing

Any help, including feedback, is highly appriciated. I have just started out with Android and I’m relatively new to app development.

1. Fork it!
2. Create your feature branch: `git checkout -b new-branch`
3. Commit your changes: `git commit -am 'Make a valuable addition'`
4. Push to the branch: `git push origin new-feature`
5. Submit a pull request :D

## Next Step

Once staretd, an app can never be completely finished. 

1. Revamp the project and make it better.
