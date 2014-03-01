package de.interoberlin.pyng.view.panels;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.controller.game.Round;
import de.interoberlin.pyng.model.geometry.Vector2;
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

    private static int       numberDistance;
    private static int       numberWidth;
    private static int       numberHeight;
    private static int       numberLine;

    public DrawingPanel(Context context)
    {
	super(context);
	surfaceHolder = getHolder();

	c = (Context) PyngController.getContext();
	r = c.getResources();

	numberDistance = (int) r.getDimension(R.dimen.numberDistance);
	numberWidth = (int) r.getDimension(R.dimen.numberWidth);
	numberHeight = (int) r.getDimension(R.dimen.numberHeight);
	numberLine = (int) r.getDimension(R.dimen.numberLine);
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

		// Set dimensions
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();

		Properties.setCanvasHeight(canvasHeight);
		Properties.setCanvasWidth(canvasWidth);

		// Set ball
		Ball b = PyngController.getBall();

		// Set colors
		Paint white = new Paint();
		Paint background = new Paint();
		Paint numberColor = new Paint();

		white.setARGB(255, 255, 255, 255);
		background.setARGB(255, 0, 0, 0);
		numberColor.setARGB(255, 255, 255, 255);

		int rgbMax = 255;
		int rgbMin = 25;
		int dMin = (int) 0.5 * numberHeight;
		int dMax = 2 * numberHeight;
		Vector2 center = new Vector2(canvasWidth / 2, 1.5f * numberHeight);
		int distance = 0;
		int rgb = 0;

		if (b != null)
		{
		    distance = b.getPos().getDistance(center);
		}

		if (distance < dMin)
		{
		    rgb = rgbMin;
		} else if (distance > dMax)
		{
		    rgb = rgbMax;
		} else
		{
		    rgb = (rgbMax * distance / (dMax - dMin));
		}

		numberColor.setARGB(255, rgb, rgb, rgb);

		// Clear canvas
		canvas.drawRect(0, 0, canvasWidth, canvasHeight, background);

		int score = Round.getInstance().getScore();

		// Draw score
		int digitOne = getNthDigit(score, 10, 1);
		int digitTwo = getNthDigit(score, 10, 2);
		int digitThree = getNthDigit(score, 10, 3);

		if (score < 10)
		{
		    int offsetX = (canvasWidth - numberWidth) / 2;
		    int offsetY = numberHeight;

		    ENumber number = getNumberByInt(digitOne);

		    for (ESegment s : number.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetX, s.getTop() + offsetY, s.getRight() + offsetX, s.getBottom() + offsetY, numberColor);
		    }
		} else if (score < 100)
		{
		    int offsetOneX = (int) ((canvasWidth + numberDistance) / 2);
		    int offsetOneY = numberHeight;

		    ENumber numberOne = getNumberByInt(digitOne);

		    for (ESegment s : numberOne.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetOneX, s.getTop() + offsetOneY, s.getRight() + offsetOneX, s.getBottom() + offsetOneY, numberColor);
		    }

		    int offsetTwoX = (int) ((canvasWidth - numberDistance) / 2 - numberWidth);
		    int offsetTwoY = numberHeight;

		    ENumber numberTwo = getNumberByInt(digitTwo);

		    for (ESegment s : numberTwo.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetTwoX, s.getTop() + offsetTwoY, s.getRight() + offsetTwoX, s.getBottom() + offsetTwoY, numberColor);
		    }
		} else if (score < 1000)
		{
		    int offsetOneX = (int) ((canvasWidth + numberWidth) / 2 + numberDistance);
		    int offsetOneY = numberHeight;

		    ENumber numberOne = getNumberByInt(digitOne);

		    for (ESegment s : numberOne.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetOneX, s.getTop() + offsetOneY, s.getRight() + offsetOneX, s.getBottom() + offsetOneY, numberColor);
		    }

		    int offsetTwoX = (canvasWidth - numberWidth) / 2;
		    int offsetTwoY = numberHeight;

		    ENumber numberTwo = getNumberByInt(digitTwo);

		    for (ESegment s : numberTwo.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetTwoX, s.getTop() + offsetTwoY, s.getRight() + offsetTwoX, s.getBottom() + offsetTwoY, numberColor);
		    }

		    int offsetThreeX = (int) (canvasWidth - (3.0f * numberWidth)) / 2 - numberDistance;
		    int offsetThreeY = numberHeight;

		    ENumber numberThree = getNumberByInt(digitThree);

		    for (ESegment s : numberThree.getSegmentList())
		    {
			canvas.drawRect(s.getLeft() + offsetThreeX, s.getTop() + offsetThreeY, s.getRight() + offsetThreeX, s.getBottom() + offsetThreeY, numberColor);
		    }
		}

		// Draw line
		int lineWidth = (int) PyngController.getContext().getResources().getDimension(R.dimen.lineWidth);
		canvas.drawRect(0, canvasHeight / 2 - lineWidth, canvasWidth, canvasHeight / 2 + lineWidth, white);

		// Draw ball
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
		    canvas.drawRect(p.getPos().getX() - p.getWidth() / 2, canvasHeight - p.getHeight(), p.getPos().getX() + p.getWidth() / 2, canvasHeight, white);
		}

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	}
    }

    public int getNthDigit(int number, int base, int n)
    {
	return (int) ((number / Math.pow(base, n - 1)) % base);
    }

    private ENumber getNumberByInt(int i)
    {
	switch (i)
	{
	    case 0:
		return ENumber.ZERO;
	    case 1:
		return ENumber.ONE;
	    case 2:
		return ENumber.TWO;
	    case 3:
		return ENumber.THREE;
	    case 4:
		return ENumber.FOUR;
	    case 5:
		return ENumber.FIVE;
	    case 6:
		return ENumber.SIX;
	    case 7:
		return ENumber.SEVEN;
	    case 8:
		return ENumber.EIGHT;
	    case 9:
		return ENumber.NINE;
	    default:
		return ENumber.ZERO;
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

	A(0, 0, numberWidth, numberLine),
	B(numberWidth - numberLine, 0, numberWidth, (numberHeight + numberLine) / 2),
	C(numberWidth - numberLine, (numberHeight - numberLine) / 2, numberWidth, numberHeight),
	D(0, numberHeight - numberLine, numberWidth, numberHeight),
	E(0, (numberHeight - numberLine) / 2, numberLine, numberHeight),
	F(0, 0, numberLine, (numberHeight + numberLine) / 2),
	G(0, (numberHeight - numberLine) / 2, numberWidth, (numberHeight + numberLine) / 2);

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