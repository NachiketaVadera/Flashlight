package android.nachiketa.flashlight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    SeekBar seekBar = null;
    TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String progress = Global.loadFile(getApplicationContext(), "settings.txt");

        seekBar = (SeekBar) findViewById(R.id.skbThreshold);
        seekBar.setProgress((int) Float.parseFloat(Objects.requireNonNull(progress).replace("f","")));
        seekBar.setOnSeekBarChangeListener(this);

        textView = (TextView) findViewById(R.id.tvThresholdValue);
        textView.setText(progress);

        ImageView imageView = (ImageView) findViewById(R.id.ivShakeGif);
        Glide.with(getApplicationContext())
                .load(R.drawable.chopflashlight)
                .into(imageView);
    }

    public void saveSettings(View view) {
        if (Global.saveFile(getApplicationContext(), "settings.txt", seekBar.getProgress() + "")) {
            Log.i("Flashlight_saveSettings", "File Saved Successfully");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            Log.e("Flashlight_saveSettings", "Error while saving file");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textView.setText(new StringBuilder().append(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Unused method
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Unused method
    }
}
