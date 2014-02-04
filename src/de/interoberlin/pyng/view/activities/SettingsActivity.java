package de.interoberlin.pyng.view.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.log.Log;
import de.interoberlin.pyng.model.settings.Settings;

public class SettingsActivity extends Activity
{
    private static Context  context;
    private static Activity activity;

    private static EditText etLogBuffer;
    private static SeekBar  sbSensitivyX;
    private static SeekBar  sbSensitivyY;
    private static EditText etURL;
    private static EditText etFrequency;
    private static CheckBox cbPost;
    private static CheckBox cbGet;
    private static Button   btnCancel;
    private static Button   btnSave;

    private static int      sensitivityX;
    private static int      sensitivityY;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_settings);
	// getActionBar().setDisplayHomeAsUpEnabled(true);

	// Get activity and context
	activity = this;
	context = getApplicationContext();

	// Get views by id
	etLogBuffer = (EditText) findViewById(R.id.etLogBuffer);
	sbSensitivyX = (SeekBar) findViewById(R.id.sbSensitivityX);
	sbSensitivyY = (SeekBar) findViewById(R.id.sbSensitivityY);
	cbPost = (CheckBox) findViewById(R.id.cbPost);
	cbGet = (CheckBox) findViewById(R.id.cbGet);
	etURL = (EditText) findViewById(R.id.etURL);
	etFrequency = (EditText) findViewById(R.id.etFrequency);
	btnCancel = (Button) findViewById(R.id.btnCancel);
	btnSave = (Button) findViewById(R.id.btnSave);
    }

    public void onResume()
    {
	super.onResume();
	draw();

	btnCancel.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		uiToast("Cancel");
		finish();
	    }
	});

	btnSave.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		Settings.setLogBuffer(Integer.valueOf(etLogBuffer.getText().toString()));
		Settings.setSensitivityX(sensitivityX);
		Settings.setSensitivityY(sensitivityY);
		Settings.setUrl(etURL.getText().toString());
		Settings.setFrequency(Integer.valueOf(etFrequency.getText().toString()));
		Settings.setPost(cbPost.isChecked());
		Settings.setGet(cbGet.isChecked());

		// Log events
		Log.debug("Settings log buffer    " + etLogBuffer.getText());
		Log.debug("Settings sensitivity x " + sensitivityX);
		Log.debug("Settings sensitivity y " + sensitivityY);
		Log.debug("Settings url " + etURL.getText());
		Log.debug("Settings frequency     " + etFrequency.getText());
		Log.debug("Settings POST     " + cbPost.isChecked());
		Log.debug("Settings GET      " + cbGet.isChecked());

		Log.info("Settings saved");
		uiToast("Settings saved");

		if ((cbPost.isChecked() || cbGet.isChecked()) && !isValidUrl(etURL.getText().toString()))
		{
		    Log.warn("Settings Invalid URL");
		}

		finish();
	    }

	    private boolean isValidUrl(String url)
	    {
		return !url.isEmpty();
	    }
	});

	sbSensitivyX.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
	{
	    @Override
	    public void onStopTrackingTouch(SeekBar seekBar)
	    {
	    }

	    @Override
	    public void onStartTrackingTouch(SeekBar seekBar)
	    {
	    }

	    @Override
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	    {
		SettingsActivity.sensitivityX = progress;
	    }
	});

	sbSensitivyY.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
	{
	    @Override
	    public void onStopTrackingTouch(SeekBar seekBar)
	    {
	    }

	    @Override
	    public void onStartTrackingTouch(SeekBar seekBar)
	    {
	    }

	    @Override
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	    {
		SettingsActivity.sensitivityY = progress;
	    }
	});
    }

    @Override
    protected void onPause()
    {
	super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.activity_basic, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	    case android.R.id.home:
	    {
		finish();
		break;
	    }
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
	    default:
	    {
		return super.onOptionsItemSelected(item);
	    }
	}

	return true;
    }

    private void draw()
    {
	activity.setTitle("Settings");

	etURL.setText(Settings.getUrl());
	etFrequency.setText(String.valueOf(Settings.getFrequency()));
	etLogBuffer.setText(String.valueOf(Settings.getLogBuffer()));
	sbSensitivyX.setProgress(Settings.getSensitivityX());
	sbSensitivyY.setProgress(Settings.getSensitivityY());
	cbPost.setChecked(Settings.isPost());
	cbGet.setChecked(Settings.isGet());
    }

    public static void uiToast(final String message)
    {
	if (Settings.isDebug())
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
    }
}