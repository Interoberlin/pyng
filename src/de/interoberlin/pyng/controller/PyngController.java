package de.interoberlin.pyng.controller;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import de.interoberlin.pyng.controller.game.Game;
import de.interoberlin.pyng.model.geometry.RandomNumber;
import de.interoberlin.pyng.model.geometry.Vector2;
import de.interoberlin.pyng.model.objects.Ball;
import de.interoberlin.pyng.model.objects.Panel;
import de.interoberlin.pyng.model.settings.Properties;

public class PyngController extends Application
{

    private static Ball    ball;
    private static Panel   panel;
    private static boolean initialized = false;

    private static Context context;

    @Override
    public void onCreate()
    {
	super.onCreate();
	context = this;
    }

    public static Context getContext()
    {
	return context;
    }

    public void start(Activity activity)
    {
	if (!initialized)
	{
	    init();
	}

	Simulation.getInstance(activity).start();
	Game.getInstance().start();
    }

    public void stop(Activity activity)
    {
	Simulation.getInstance(activity).stop();
	Game.getInstance().stop();
    }

    public static void init()
    {
	initialized = true;
	float direction = RandomNumber.getRandomNumber(135, 225);

	ball = new Ball(new Vector2(Properties.getCanvasWidth() / 2, Properties.getCanvasHeight() / 2), direction, 3);
	panel = new Panel(new Vector2(Properties.getCanvasWidth() / 2, 0));
    }

    public boolean isGameRunning()
    {
	return Game.getInstance().isRunning();
    }

    public static Ball getBall()
    {
	return ball;
    }

    public static Panel getPanel()
    {
	return panel;
    }

    /**
     * Moves ball one step forward
     */
    public static void step()
    {
	int w = Properties.getCanvasWidth();
	int h = Properties.getCanvasHeight();
	int ballRadius = Properties.getMinDimension() / 36;

	// Update position
	float x = (float) (ball.getPos().getX() + ball.getSpeed() * Math.sin(Math.toRadians(ball.getDirection())));
	float y = (float) (ball.getPos().getY() + ball.getSpeed() * Math.cos(Math.toRadians(ball.getDirection())));
	ball.setPos(new Vector2(x, y));

	// Check top border
	if (ball.getPos().getY() <= ballRadius)
	{
	    ball.setDirection((ball.getDirection() - 180) * -1);
	}

	// Check left and right border
	if ((ball.getPos().getX() <= ballRadius) || (ball.getPos().getX() >= w - ballRadius))
	{
	    ball.setDirection(360 - ball.getDirection());
	}

	// Check bottom border
	if (ball.getPos().getY() >= h - ballRadius)
	{
	    // Game.getInstance().stop();
	    init();
	}

	// Check panel
	float panelWidth = Properties.getMinDimension() / 8;
	float panelHeight = Properties.getMinDimension() / 40;
	float ballX = ball.getPos().getX();
	float ballY = ball.getPos().getY();
	float topEdge = Properties.getCanvasHeight() - panelHeight;
	float leftEdge = panel.getPos().getX() - panelWidth / 2;
	float rightEdge = panel.getPos().getX() + panelWidth / 2;

	if ((ballY + ballRadius > topEdge) && (ballX > leftEdge) && (ballX < rightEdge))
	{
	    ball.setDirection((ball.getDirection() - 180) * -1);
	    ball.setSpeed(ball.getSpeed() + 1.02f);
	}

	// Normalize direction
	while (ball.getDirection() < 0)
	{
	    ball.setDirection(ball.getDirection() + 360);
	}

	while (ball.getDirection() >= 360)
	{
	    ball.setDirection(ball.getDirection() - 360);
	}
    }

    public static void setPanelPos()
    {
	float x = Properties.getCanvasWidth() / 2 - (Simulation.getRawX() * Properties.getCanvasWidth() / 10);
	float panelWidth = Properties.getMinDimension() / 8;

	if (x < panelWidth / 2)
	{
	    x = panelWidth / 2;
	}
	if (x > Properties.getCanvasWidth() - panelWidth / 2)
	{
	    x = Properties.getCanvasWidth() - panelWidth / 2;
	}

	panel.setPos(new Vector2(x, 0));

    }
}
