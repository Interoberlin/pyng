package de.interoberlin.pyng.view.panels;

import java.util.ArrayList;
import java.util.List;

import tv.ouya.console.api.OuyaController;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.controller.game.Round;
import de.interoberlin.pyng.model.objects.Ball;
import de.interoberlin.pyng.model.objects.Panel;
import de.interoberlin.pyng.model.settings.Properties;

public class DrawingPanel extends SurfaceView implements Runnable
{
    Thread		   thread  = null;
    SurfaceHolder	    surfaceHolder;
    private static boolean   running = false;

    private static Context   c;
    private static Resources r;

    private static int       w;
    private static int       h;
    private static int       l;

    public DrawingPanel(Context context)
    {
	super(context);
	surfaceHolder = getHolder();

	c = (Context) PyngController.getContext();
	r = c.getResources();

	w = (int) r.getDimension(R.dimen.numberWidth);
	h = (int) r.getDimension(R.dimen.numberHeight);
	l = (int) r.getDimension(R.dimen.numberLine);

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

		if (PyngController.isKeyPressed(OuyaController.BUTTON_O))
		{
		    background.setARGB(255, 0, 0, 255);
		} else if (PyngController.isKeyPressed(OuyaController.BUTTON_U))
		{
		    background.setARGB(255, 0, 255, 0);
		} else
		{
		    background.setARGB(255, 0, 0, 0);
		}

		// Set dimensions
		int width = Properties.getCanvasWidth();
		int height = Properties.getCanvasHeight();

		// Clear canvas
		canvas.drawRect(0, 0, width, height, background);

		int score = Round.getInstance().getPoints();

		// Draw score
		int digitOne = score % 10;
		int digitTwo = score / 10;
//		int digitThree = score / 100;
		
		if (score < 10)
		{
		    int offsetX = (width - w) / 2;
		    int offsetY = h;
		    
		    ENumber number = getNumberByInt(digitOne);

		    for (ESegment s : number.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetX, s.getTop() + offsetY, s.getRight() + offsetX, s.getBottom() + offsetY, white);
		    }
		}
		else if (score < 100)
		{
		    int offsetOneX = (int) (width + (1.5f * w)) / 2;
		    int offsetOneY = h;
		    
		    ENumber numberOne = getNumberByInt(digitOne);

		    for (ESegment s : numberOne.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetOneX, s.getTop() + offsetOneY, s.getRight() + offsetOneX, s.getBottom() + offsetOneY, white);
		    }
		    
		    int offsetTwoX = (int) (width - (1.5f * w)) / 2;
		    int offsetTwoY = h;
		    
		    ENumber numberTwo = getNumberByInt(digitTwo);

		    for (ESegment s : numberTwo.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetTwoX, s.getTop() + offsetTwoY, s.getRight() + offsetTwoX, s.getBottom() + offsetTwoY, white);
		    }
		}

		// Draw line
		int lineWidth = (int) PyngController.getContext().getResources().getDimension(R.dimen.lineWidth);
		canvas.drawRect(0, height / 2 - lineWidth, width, height / 2 + lineWidth, white);

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
		    canvas.drawRect(p.getPos().getX() - p.getWidth() / 2, height - p.getHeight(), p.getPos().getX() + p.getWidth() / 2, height, white);
		}

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	}
    }
    
    private ENumber getNumberByInt(int i)
    {
	    switch (i)
	    {
		case 0 : return  ENumber.ZERO;
		case 1 : return  ENumber.ONE;
		case 2 : return  ENumber.TWO; 
		case 3 : return  ENumber.THREE;
		case 4 : return  ENumber.FOUR; 
		case 5 : return  ENumber.FIVE; 
		case 6 : return  ENumber.SIX; 
		case 7 : return  ENumber.SEVEN; 
		case 8 : return  ENumber.EIGHT; 
		case 9 : return  ENumber.NINE;
		default : return ENumber.ZERO;
	    }
    }

    private enum ENumber
    {
	ZERO(ESegment.A, ESegment.B, ESegment.C, ESegment.D, ESegment.E, ESegment.F),
	ONE(ESegment.B, ESegment.C),
	TWO(ESegment.A, ESegment.B, ESegment.G, ESegment.E, ESegment.D),
	THREE(ESegment.A, ESegment.B, ESegment.C, ESegment.D, ESegment.G),
	FOUR(ESegment.F, ESegment.G, ESegment.B, ESegment.C),
	FIVE(ESegment.A, ESegment.F, ESegment.G, ESegment.C, ESegment.D),
	SIX(ESegment.A, ESegment.F, ESegment.G, ESegment.C, ESegment.D, ESegment.E),
	SEVEN(ESegment.A, ESegment.B, ESegment.C),
	EIGHT(ESegment.A, ESegment.B, ESegment.C, ESegment.D, ESegment.E, ESegment.F, ESegment.G),
	NINE(ESegment.A, ESegment.B, ESegment.C, ESegment.D, ESegment.F, ESegment.G);

	List<ESegment> segmentList = new ArrayList<ESegment>();

	ENumber(ESegment... segments)
	{
	    for (ESegment s : segments)
	    {
		segmentList.add(s);
	    }
	}

	public List<ESegment> getSegmentList()
	{
	    return segmentList;
	}
    }

    private enum ESegment
    {

	A(0, 0, w, l), B(w - l, 0, w, (h + l) / 2), C(w - l, (h - l) / 2, w, h), D(0, h - l, w, h), E(0, (h - l) / 2, l, h), F(0, 0, l, (h + l) / 2), G(0, (h - l) / 2, w,
		(h + l) / 2);

	private float left;
	private float top;
	private float right;
	private float bottom;

	ESegment(int left, int top, int right, int bottom)
	{
	    this.left = left;
	    this.top = top;
	    this.right = right;
	    this.bottom = bottom;
	}

	public float getLeft()
	{
	    return left;
	}

	public float getTop()
	{
	    return top;
	}

	public float getRight()
	{
	    return right;
	}

	public float getBottom()
	{
	    return bottom;
	}
    }
}