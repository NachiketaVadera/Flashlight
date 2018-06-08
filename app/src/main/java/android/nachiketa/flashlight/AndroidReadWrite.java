package android.nachiketa.flashlight;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class AndroidReadWrite {

    public static boolean saveFile(Context context, String fileName, String data) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(data);
            out.close();
            Log.i("saveFile", "File saved successfully");
            return true;
        } catch (IOException ioEx) {
            Log.e("saveFile", ioEx.getMessage());
            return false;
        }
    }

    public static String loadFile(Context context, String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = bufferedReader.readLine();
            bufferedReader.close();
            Log.i("loadFile", "File read successfully");
            return line;
        } catch (IOException ioEx) {
            Log.e("loadFile", ioEx.getMessage());
            return null;
        }
    }

}
