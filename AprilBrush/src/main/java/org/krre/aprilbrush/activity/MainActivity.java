package org.krre.aprilbrush.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import org.krre.aprilbrush.R;
import org.krre.aprilbrush.data.GlobalVar;
import org.krre.aprilbrush.logic.BrushEngine;
import org.krre.aprilbrush.view.PaintView;

public class MainActivity extends Activity {
    private int memoryClass;
    private String TAG = "AB";
    private BrushEngine brushEngine;
    private ColorpickerFragment colorpickerFragment = new ColorpickerFragment();
    private BrushSettingsFragment brushSettingsFragment = new BrushSettingsFragment();
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        PaintView paintView = (PaintView)findViewById(R.id.paintView);
        brushEngine = paintView.getBrushEngine();

        ToggleButton penToggleButton = (ToggleButton)findViewById(R.id.penToggleButton);
        penToggleButton.setChecked(GlobalVar.getInstance().isPenMode());

        ActivityManager activityManager = (ActivityManager)getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        memoryClass = activityManager.getLargeMemoryClass();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bitmap bufferBitmap = brushEngine.getBufferBitmap();
        outState.putParcelable("bufferBitmap", bufferBitmap);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Bitmap bufferBitmap = savedInstanceState.getParcelable("bufferBitmap");
        if (bufferBitmap != null) {
            int orientation = getResources().getConfiguration().orientation;
            brushEngine.setBitmap(bufferBitmap, orientation);
        }
    }

    public void onPenToggleClick(View v) {
        ToggleButton tb = (ToggleButton)v;
        GlobalVar.getInstance().setPenMode(tb.isChecked());
    }

    public void onColorButtonClick(View v) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment currentFragment = getFragmentManager().findFragmentByTag("colorpicker");
        if (currentFragment != null) {
            fragmentTransaction.remove(colorpickerFragment);
        } else {
            fragmentTransaction.remove(brushSettingsFragment);
            fragmentTransaction.replace(R.id.colorpickerFrame, colorpickerFragment, "colorpicker");
        }

        fragmentTransaction.commit();
    }

    public void onBrushButtonClick(View v) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment currentFragment = getFragmentManager().findFragmentByTag("brush-settings");
        if (currentFragment != null) {
            fragmentTransaction.remove(brushSettingsFragment);
        } else {
            fragmentTransaction.remove(colorpickerFragment);
            fragmentTransaction.replace(R.id.brushSettingsFrame, brushSettingsFragment, "brush-settings");
        }

        fragmentTransaction.commit();
    }

    public void onClearButtonClick(View v) {
        brushEngine.clear();
    }
}
