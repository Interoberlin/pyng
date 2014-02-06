package de.interoberlin.pyng.controller.game;

import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.view.activities.PyngActivity;

public class Game implements Runnable
{
    private static Game game;

    private Thread      thread  = null;
    private boolean     running = false;

    private Game()
    {
    }

    public static Game getInstance()
    {
	if (game == null)
	{
	    game = new Game();
	}

	return game;
    }

    public void start()
    {
	PyngActivity.uiToast("Game started");
	running = true;
	thread = new Thread(this);
	thread.start();
    }

    public void stop()
    {
	PyngActivity.uiToast("Game stopped");
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

    public boolean isRunning()
    {
	return running;
    }

    @Override
    public void run()
    {
	while (running)
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