package de.interoberlin.pyng.controller.game;

import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.view.activities.PyngActivity;

public class Round implements Runnable
{
    private static Round     round;

    private Thread	   thread      = null;
    private boolean	  running     = false;
    private boolean	  pausedBall  = false;
    private boolean	  pausedPanel = false;

    private int	      score;
    private static final int INIT_SCORE  = 0;

    private Round()
    {
	setScore(INIT_SCORE);
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
	setScore(INIT_SCORE);
	PyngController.init();
    }

    public void start()
    {
	PyngActivity.uiToast("Round started");
	running = true;
	thread = new Thread(this);
	thread.start();
    }

    public void stop()
    {
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

    public void pausePanel()
    {
	pausedPanel = true;
    }

    public void pauseBall()
    {
	pausedBall = true;
    }

    public void resumePanel()
    {
	pausedPanel = false;
    }

    public void resumeBall()
    {
	pausedBall = false;
    }

    public boolean isRunning()
    {
	return running;
    }

    public boolean isBallPaused()
    {
	return pausedBall;
    }

    public boolean isPanelPaused()
    {
	return pausedPanel;
    }

    @Override
    public void run()
    {
	int fps = PyngController.getDesiredFPS();
	long millisBefore = 0;
	long millisAfter = 0;
	long millisFrame = 1000 / fps;

	while (running)
	{
	    millisBefore = System.currentTimeMillis();

	    // if (millisBefore - millisAfter > millisFrame)
	    // {
	    if ((millisBefore - millisAfter) != 0)
	    {
		PyngController.setRealFPS((int) (fps * millisFrame / (millisBefore - millisAfter)));
	    }

	    if (!pausedBall) PyngController.updateBall();
	    if (!pausedPanel) PyngController.updatePanel();
	    
	    millisAfter = System.currentTimeMillis();

	    try
	    {
		Thread.sleep(10);
	    } catch (InterruptedException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    // }
	}

    }

    public int getScore()
    {
	return score;
    }

    public void setScore(int score)
    {
	this.score = score;
    }

    public void incrementScore()
    {
	this.score++;
    }
}