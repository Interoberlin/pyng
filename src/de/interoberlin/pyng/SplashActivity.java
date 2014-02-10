/**
 * 
 */
package de.interoberlin.pyng;

import tv.ouya.console.api.OuyaController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.view.activities.PyngActivity;

public class SplashActivity extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_splash);

	OuyaController.init(PyngController.getContext());
	
	Thread timer = new Thread()
	{
	    public void run()
	    {
		try
		{
		    sleep(1500);
		} catch (InterruptedException e)
		{
		    e.printStackTrace();
		} finally
		{
		    Intent openStartingPoint = new Intent(SplashActivity.this, PyngActivity.class);
		    startActivity(openStartingPoint);
		}
	    }
	};
	timer.start();
    }

    @Override
    protected void onPause()
    {
	super.onPause();
	finish();
    }
}
