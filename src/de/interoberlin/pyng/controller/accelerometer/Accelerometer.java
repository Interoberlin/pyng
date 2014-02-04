package de.interoberlin.pyng.controller.accelerometer;

import java.util.Observable;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import de.interoberlin.pyng.controller.Simulation;
import de.interoberlin.pyng.controller.log.Log;
import de.interoberlin.pyng.view.activities.PyngActivity;

public class Accelerometer extends Observable implements SensorEventListener
{
    private Activity	     activity;

    private Sensor	       accelerometer;
    private float		sensorX;
    private float		sensorY;

    private static Accelerometer instance;

    private Accelerometer(Activity activity)
    {
	accelerometer = ((PyngActivity) activity).getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	this.activity = activity;

	addObserver(Simulation.getInstance(activity));
    }

    public static Accelerometer getInstance(Activity activity)
    {
	if (instance == null)
	{
	    instance = new Accelerometer(activity);
	}

	return instance;
    }

    public void start()
    {
	PyngActivity.uiToast("Accelerometer started");
	Log.info("Accelerometer started");
	((PyngActivity) activity).getSensorManager().registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop()
    {
	PyngActivity.uiToast("Accelerometer stopped");
	Log.info("Accelerometer stopped");
	((PyngActivity) activity).getSensorManager().unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
	if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
	{
	    return;
	}

	switch (((PyngActivity) activity).getDisplay().getRotation())
	{
	    case Surface.ROTATION_0:
	    {
		sensorX = event.values[0];
		sensorY = -event.values[1];
		break;
	    }
	    case Surface.ROTATION_90:
	    {
		sensorX = -event.values[1];
		sensorY = -event.values[0];
		break;
	    }
	    case Surface.ROTATION_180:
	    {
		sensorX = -event.values[0];
		sensorY = event.values[1];
		break;
	    }
	    case Surface.ROTATION_270:
	    {
		sensorX = event.values[1];
		sensorY = event.values[0];
		break;
	    }
	}

	setChanged();
	notifyObservers(new AccelerationEvent(sensorX, sensorY));
	PyngActivity.uiDraw();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}