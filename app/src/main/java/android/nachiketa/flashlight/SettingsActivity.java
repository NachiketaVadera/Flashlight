package android.nachiketa.flashlight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    SeekBar seekBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBar = (SeekBar) findViewById(R.id.skbThreshold);
    }

    public void saveSettings(View view) {
        if (AndroidReadWrite.saveFile(getApplicationContext(), "settings.txt", seekBar.getProgress() + "")) {
            Log.i("Flashlight_saveSettings", "File Saved Successfully");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            Log.e("Flashlight_saveSettings", "Error while saving file");
        }
    }
}
