package de.interoberlin.pyng.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.Simulation;
import de.interoberlin.pyng.controller.activities.PyngController;
import de.interoberlin.pyng.model.settings.Settings;
import de.interoberlin.pyng.view.panels.DrawingPanel;

public class PyngActivity extends Activity
{
    private static Context       context;
    private static Activity      activity;

    private static SensorManager mSensorManager;
    private WindowManager	mWindowManager;
    private static Display       mDisplay;

    private static DrawingPanel  srfc;

    private static LinearLayout  lnr;

    private static LinearLayout  oneLnr;
    private static TextView      oneTvFirst;
    private static TextView      oneTvSecond;
    private static TextView      oneTvThird;
    private static TextView      oneTvFourth;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	// Get activity and context
	activity = this;
	context = getApplicationContext();

	// Get an instance of the SensorManager
	mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

	// Get an instance of the WindowManager
	mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	mDisplay = mWindowManager.getDefaultDisplay();

	// Add surface view
	srfc = new DrawingPanel(activity);
	activity.addContentView(srfc, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	// Add linear layout
	lnr = new LinearLayout(activity);
	activity.addContentView(lnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

	// Start the simulation
	Simulation.getInstance(activity).start();
    }

    public void onResume()
    {
	super.onResume();
	srfc.onResume();

	draw();

	srfc.setOnTouchListener(new OnTouchListener()
	{

	    @Override
	    public boolean onTouch(View v, MotionEvent event)
	    {
		if (DrawingPanel.isRunning())
		{
		    srfc.onPause();
		} else
		{
		    srfc.onResume();
		}

		return false;
	    }
	});
    }

    @Override
    protected void onPause()
    {
	super.onPause();
	srfc.onPause();
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();

	// Stop the simulartion
	Simulation.getInstance(activity).stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	    case R.id.menu_debug:
	    {
		if (Settings.isDebug())
		{
		    uiToast("Debug disabled");
		    Settings.setDebug(false);
		} else
		{
		    Settings.setDebug(true);
		    uiToast("Debug enabled");
		}
		break;
	    }
	    case R.id.menu_log:
	    {
		Intent i = new Intent(PyngActivity.this, LogActivity.class);
		startActivity(i);
		break;
	    }
	    case R.id.menu_settings:
	    {
		Intent i = new Intent(PyngActivity.this, SettingsActivity.class);
		startActivity(i);
		break;
	    }
	    case R.id.menu_support:
	    {
		Intent i = new Intent(PyngActivity.this, SupportActivity.class);
		startActivity(i);
		break;
	    }
	    default:
	    {
		return super.onOptionsItemSelected(item);

	    }
	}

	return true;
    }

    public static void draw()
    {
	activity.setTitle(R.string.app_name);

	if (lnr != null)
	{
	    lnr.removeAllViews();

	    // Add text views
	    oneLnr = new LinearLayout(activity);
	    oneTvFirst = new TextView(activity);
	    oneTvSecond = new TextView(activity);
	    oneTvThird = new TextView(activity);
	    oneTvFourth = new TextView(activity);
	    oneTvFirst.setText(R.string.ball);
	    oneTvSecond.setText(String.valueOf(PyngController.getBallPos().getX()));
	    oneTvThird.setText(String.valueOf(PyngController.getBallPos().getY()));
	    oneTvFourth.setText(String.valueOf(PyngController.getBallDirection()));
	    oneLnr.addView(oneTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    oneLnr.addView(oneTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    oneLnr.addView(oneTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    oneLnr.addView(oneTvFourth, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

	    lnr.setOrientation(1);
	    lnr.addView(oneLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
    }

    public SensorManager getSensorManager()
    {
	return mSensorManager;
    }

    public Display getDisplay()
    {
	return mDisplay;
    }

    public static void uiToast(final String message)
    {
	activity.runOnUiThread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	    }
	});
    }

    public static void uiDraw()
    {
	activity.runOnUiThread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		draw();
	    }
	});
    }
}