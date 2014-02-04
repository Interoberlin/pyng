package de.interoberlin.pyng.controller.activities;

import de.interoberlin.pyng.model.geometry.Vector2;
import de.interoberlin.pyng.model.settings.Properties;

public class PyngController
{

    private static final float BALL_SPEED    = 5f;
    private static int	 ballDirection = 45;
    private static Vector2     ballPos       = new Vector2(0, 0);

    public static void init()
    {
	ballPos = new Vector2(Properties.getCanvasWidth() / 2, Properties.getCanvasHeight() / 2);
    }

    public static void resetRound()
    {
	ballPos.setX(Properties.getCanvasWidth() / 2);
	ballPos.setY(Properties.getCanvasHeight() / 2);
    }

    public static Vector2 getBallPos()
    {
	return ballPos;
    }

    public static int getBallDirection()
    {
	return ballDirection;
    }

    public static void step()
    {
	int w = Properties.getCanvasWidth();
	int h = Properties.getCanvasHeight();
	int ballRadius = Properties.getMinDimension() / 36;
	
	ballPos.setX((float) (ballPos.getX() + (BALL_SPEED * Math.sin(Math.toRadians(ballDirection)))));
	ballPos.setY((float) (ballPos.getY() + (BALL_SPEED * Math.cos(Math.toRadians(ballDirection)))));

	// Check top border
	if (ballPos.getY() <= ballRadius)
	{
	    ballDirection -= 180;
	    ballDirection *= -1;
	}

	// Check left border
	if (ballPos.getX() <= ballRadius)
	{
	    ballDirection = 360 - ballDirection;
	}
	
	// Check right border
	if (ballPos.getX() >= w-ballRadius)
	{
	    ballDirection = 360 - ballDirection;
	}
	
	// Check bottom border
	if (ballPos.getY() >= h-ballRadius)
	{
	    ballDirection -= 180;
	    ballDirection *= -1;
	}
	
	while (ballDirection < 0)
	{
	    ballDirection += 360;
	}
	
	while (ballDirection >= 360)
	{
	    ballDirection -= 360;
	}
    }
}
