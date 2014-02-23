package de.interoberlin.pyng.controller.game;

import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.view.activities.PyngActivity;

public class Round implements Runnable
{
    private static Round     round;

    private Thread	   thread      = null;
    private boolean	  running     = false;
    private boolean	  paused      = false;

    private int	      points;
    private static final int INIT_POINTS = 0;

    private Round()
    {
	setPoints(INIT_POINTS);
    }

    public static Round getInstance()
    {
	if (round == null)
	{
	    round = new Round();
	}

	return round;
    }

    public void init()
    {
	setPoints(INIT_POINTS);
	PyngController.init();
    }

    public void start()
    {
	System.out.println("Interoberlin Round start");

	PyngActivity.uiToast("Round started");
	running = true;
	thread = new Thread(this);
	thread.start();
    }

    public void stop()
    {
	System.out.println("Interoberlin Round stop");

	PyngActivity.uiToast("Round stopped");
	boolean retry = true;
	running = false;

	while (retry)
	{
	    try
	    {
		thread.join();
		retry = false;
	    } catch (InterruptedException e)
	    {
		e.printStackTrace();
	    }
	}
    }

    public void pause()
    {
	System.out.println("Interoberlin Round pause");
	paused = true;
    }

    public void resume()
    {
	System.out.println("Interoberlin Round resume");
	paused = false;
    }

    public boolean isRunning()
    {
	return running;
    }

    public boolean isPaused()
    {
	return paused;
    }

    @Override
    public void run()
    {
	while (running)
	{
	    if (!paused)
	    {
		try
		{
		    Thread.sleep(10);
		} catch (InterruptedException e)
		{
		    e.printStackTrace();
		}

		PyngController.step();
		PyngController.setPanelPos();
	    }
	}
    }

    public int getPoints()
    {
	return points;
    }

    public void setPoints(int points)
    {
	this.points = points;
    }

    public void incrementPoints()
    {
	this.points++;
    }
}