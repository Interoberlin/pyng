package de.interoberlin.pyng.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
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

import de.interoberlin.mate.lib.view.AboutActivity;
import de.interoberlin.mate.lib.view.LogActivity;
import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.controller.accelerometer.AcceleratorListener;
import de.interoberlin.pyng.controller.game.Round;
import de.interoberlin.pyng.controller.log.Log;
import de.interoberlin.pyng.model.settings.Settings;
import de.interoberlin.pyng.model.sound.ESound;
import de.interoberlin.pyng.view.panels.DrawingPanel;

public class PyngActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;
	private static PyngController	controller;

	private static SensorManager	mSensorManager;
	private WindowManager			mWindowManager;
	private static Display			mDisplay;

	private static DrawingPanel		panel;

	private static LinearLayout		lnr;

	private static LinearLayout		oneLnr;
	private static TextView			oneTvFirst;
	private static TextView			oneTvSecond;
	private static TextView			oneTvThird;
	private static TextView			oneTvFourth;

	private static LinearLayout		twoLnr;
	private static TextView			twoTvFirst;
	private static TextView			twoTvSecond;
	private static TextView			twoTvThird;
	private static TextView			twoTvFourth;

	private static LinearLayout		threeLnr;
	private static TextView			threeTvFirst;
	private static TextView			threeTvSecond;
	private static TextView			threeTvThird;
	private static TextView			threeTvFourth;

	private static LinearLayout		fourLnr;
	private static TextView			fourTvFirst;
	private static TextView			fourTvSecond;
	private static TextView			fourTvThird;
	private static TextView			fourTvFourth;

	private static SoundPool		soundPool;
	private static int				soundBing;
	private static int				soundBong;
	private static int				soundGameOver;
	private static boolean			loaded	= false;

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
		panel = new DrawingPanel(activity);
		activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Add linear layout
		lnr = new LinearLayout(activity);
		activity.addContentView(lnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sound
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener()
		{
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
			{
				loaded = true;
			}
		});
		soundBing = soundPool.load(this, R.raw.bing, 1);
		soundBong = soundPool.load(this, R.raw.bong, 1);
		soundGameOver = soundPool.load(this, R.raw.gameover, 1);

		// Get controller
		controller = (PyngController) getApplicationContext();
	}

	public void onResume()
	{
		super.onResume();
		panel.onResume();

		// controller.start(activity);

		draw();

		panel.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!controller.isGameRunning())
				{
					controller.start(activity);
				} else
				{
					if (controller.isRoundPaused())
					{
						controller.resume(activity);
					} else
					{
						controller.pause(activity);
					}
				}

				return false;
			}
		});
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		panel.onPause();

		controller.stop(activity);
		AcceleratorListener.getInstance(activity).stop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		controller.stop(activity);
		AcceleratorListener.getInstance(activity).stop();
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
				Intent i = new Intent(PyngActivity.this, AboutActivity.class);
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

			twoLnr = new LinearLayout(activity);
			twoTvFirst = new TextView(activity);
			twoTvSecond = new TextView(activity);
			twoTvThird = new TextView(activity);
			twoTvFourth = new TextView(activity);

			threeLnr = new LinearLayout(activity);
			threeTvFirst = new TextView(activity);
			threeTvSecond = new TextView(activity);
			threeTvThird = new TextView(activity);
			threeTvFourth = new TextView(activity);

			fourLnr = new LinearLayout(activity);
			fourTvFirst = new TextView(activity);
			fourTvSecond = new TextView(activity);
			fourTvThird = new TextView(activity);
			fourTvFourth = new TextView(activity);

			if (PyngController.getBall() != null)
			{
				oneTvFirst.setText(R.string.ball);
				oneTvSecond.setText(String.valueOf(PyngController.getBall().getPos().getX()));
				oneTvThird.setText(String.valueOf(PyngController.getBall().getPos().getY()));
				oneTvFourth.setText(String.valueOf(PyngController.getBall().getDirection()));
			}

			twoTvFirst.setText(R.string.tilt);
			twoTvSecond.setText(String.valueOf(AcceleratorListener.getRawX()));
			twoTvThird.setText(String.valueOf(AcceleratorListener.getRawY()));
			twoTvFourth.setText("");

			threeTvFirst.setText(R.string.score);
			threeTvSecond.setText(String.valueOf(Round.getInstance().getScore()));
			threeTvThird.setText("");
			threeTvFourth.setText("");

			fourTvFirst.setText(R.string.fps);
			fourTvSecond.setText(String.valueOf(PyngController.getRealFPS()));
			fourTvThird.setText("");
			fourTvFourth.setText("");

			oneLnr.addView(oneTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			oneLnr.addView(oneTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			oneLnr.addView(oneTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			oneLnr.addView(oneTvFourth, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			twoLnr.addView(twoTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			twoLnr.addView(twoTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			twoLnr.addView(twoTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			twoLnr.addView(twoTvFourth, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			threeLnr.addView(threeTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			threeLnr.addView(threeTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			threeLnr.addView(threeTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			threeLnr.addView(threeTvFourth, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			fourLnr.addView(fourTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			fourLnr.addView(fourTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			fourLnr.addView(fourTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			fourLnr.addView(fourTvFourth, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

			lnr.setOrientation(LinearLayout.VERTICAL);
			lnr.addView(oneLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(twoLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(threeLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			lnr.addView(fourLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		PyngController.indicateKey(keyCode, true);
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		PyngController.indicateKey(keyCode, false);
		return true;
	}

	public static boolean playSound(ESound sound)
	{
		// Getting the user sound settings
		AudioManager audioManager = (AudioManager) PyngController.getContext().getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;

		// Is the sound loaded already?
		if (loaded)
		{
			switch (sound)
			{
				case BING:
				{
					soundPool.play(soundBing, volume, volume, 1, 0, 1f);
					Log.trace("Played sound " + sound.toString());
					break;
				}
				case BONG:
				{
					soundPool.play(soundBong, volume, volume, 1, 0, 1f);
					Log.trace("Played sound " + sound.toString());
					break;
				}
				case GAME_OVER:
				{
					soundPool.play(soundGameOver, volume, volume, 1, 0, 1f);
					Log.trace("Played sound " + sound.toString());
					break;
				}
				default:
					break;
			}
		}

		return false;
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