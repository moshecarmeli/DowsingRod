package edu.ucsb.cs.capstone.letmypeoplecode.dowsingrod;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import edu.ucsb.cs.capstone.letmypeoplecode.dowsingrod.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class DetectionLauncher extends Activity {
    Handler handler = new Handler();
    private BluetoothAdapter mBluetoothAdapter;
    private static final int RESULT_SETTINGS = 1;
    private final static int REQUEST_ENABLE_BT = 1;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_launcher);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Hook in settings
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // Example of using the preferences from other project:
        // this.samplingRateInMilliseconds = Integer.parseInt(sharedPref.getString("sampling_rate", "20"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent i = new Intent(this, SettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;

        }

        return true;
    }

    public void genSound(View view) {

        final NoiseGenerator noise = new NoiseGenerator();
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                noise.genTone();
                handler.post(new Runnable() {

                    public void run() {
                        noise.playSound();
                    }
                });
            }
        });
        thread.start();
    }

    public SharedPreferences getSharedPref() {
        return sharedPref;
    }

    public void setSharedPref(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }
}
