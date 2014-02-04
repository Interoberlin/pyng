package de.interoberlin.pyng.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.pyng.controller.activities.PyngController;
import de.interoberlin.pyng.model.settings.Properties;

public class DrawingPanel extends SurfaceView implements Runnable
{
    public static boolean isRunning()
    {
	return running;
    }

    Thread		 thread  = null;
    SurfaceHolder	  surfaceHolder;
    private static boolean running = false;

    public DrawingPanel(Context context)
    {
	super(context);
	surfaceHolder = getHolder();
    }

    public void onChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
    }

    public void onResume()
    {
	running = true;
	thread = new Thread(this);
	thread.start();
    }

    public void onPause()
    {
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

    @Override
    public void run()
    {
	while (!running)
	{

	}

	init();

	while (running)
	{
	    if (surfaceHolder.getSurface().isValid())
	    {
		Canvas canvas = surfaceHolder.lockCanvas();

		Properties.setCanvasHeight(canvas.getHeight());
		Properties.setCanvasWidth(canvas.getWidth());

		float ballX = PyngController.getBallPos().getX();
		float ballY = PyngController.getBallPos().getY();
		int ballRadius = Properties.getMinDimension() / 36;
		int lineWidth = Properties.getMinDimension() / 126;

		Paint white = new Paint();
		Paint background = new Paint();
		Paint orange = new Paint();

		white.setARGB(255, 255, 255, 255);
		orange.setARGB(255, 238, 118, 0);
		background.setARGB(255, 0, 0, 0);

		int w = Properties.getCanvasWidth();
		int h = Properties.getCanvasHeight();

		// Clear
		canvas.drawRect(0, 0, w, h, background);

		// Draw line
		canvas.drawRect(0, h / 2 - lineWidth, w, h / 2 + lineWidth, white);

		// Draw Point
		canvas.drawCircle(ballX, ballY, ballRadius, white);

		// Step
		PyngController.step();

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	}
    }

    private void init()
    {
	if (surfaceHolder.getSurface().isValid())
	{
	    Canvas canvas = surfaceHolder.lockCanvas();

	    Properties.setCanvasHeight(canvas.getHeight());
	    Properties.setCanvasWidth(canvas.getWidth());

	    surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	PyngController.init();
    }
}