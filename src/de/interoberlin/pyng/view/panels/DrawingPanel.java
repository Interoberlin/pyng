package de.interoberlin.pyng.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.model.objects.Ball;
import de.interoberlin.pyng.model.objects.Panel;
import de.interoberlin.pyng.model.settings.Properties;

public class DrawingPanel extends SurfaceView implements Runnable
{
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

    public static boolean isRunning()
    {
	return running;
    }

    @Override
    public void run()
    {
	while (running)
	{
	    if (surfaceHolder.getSurface().isValid())
	    {
		// Lock canvas
		Canvas canvas = surfaceHolder.lockCanvas();

		Properties.setCanvasHeight(canvas.getHeight());
		Properties.setCanvasWidth(canvas.getWidth());

		// Set colors
		Paint white = new Paint();
		Paint background = new Paint();

		white.setARGB(255, 255, 255, 255);

		if (PyngController.isButtonU())
		{
		    background.setARGB(255, 0, 0, 255);
		} else if (PyngController.isButtonO())
		{
		    background.setARGB(255, 0, 255, 0);
		} else
		{
		    background.setARGB(255, 0, 0, 0);
		}

		// Set dimensions
		int w = Properties.getCanvasWidth();
		int h = Properties.getCanvasHeight();

		// Clear canvas
		canvas.drawRect(0, 0, w, h, background);

		// Draw line
		int lineWidth = (int) PyngController.getContext().getResources().getDimension(R.dimen.lineWidth);
		canvas.drawRect(0, h / 2 - lineWidth, w, h / 2 + lineWidth, white);

		// Draw ball
		Ball b = PyngController.getBall();
		if (b != null)
		{
		    float ballX = b.getPos().getX();
		    float ballY = b.getPos().getY();
		    int ballRadius = b.getRadius();
		    canvas.drawCircle(ballX, ballY, ballRadius, white);
		}

		// Draw Panel
		Panel p = PyngController.getPanel();
		if (p != null)
		{
		    canvas.drawRect(p.getPos().getX() - p.getWidth() / 2, h - p.getHeight(), p.getPos().getX() + p.getWidth() / 2, h, white);
		}

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	}
    }
}