package de.interoberlin.pyng.controller;

import tv.ouya.console.api.OuyaController;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import de.interoberlin.pyng.controller.accelerometer.AcceleratorListener;
import de.interoberlin.pyng.controller.game.Game;
import de.interoberlin.pyng.controller.game.Round;
import de.interoberlin.pyng.controller.log.Log;
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

    private static boolean buttonO     = false;
    private static boolean buttonU     = false;
    private static boolean buttonY     = false;
    private static boolean buttonA     = false;

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

	AcceleratorListener.getInstance(activity).start();
	Game.getInstance().start();
    }

    public void stop(Activity activity)
    {
	AcceleratorListener.getInstance(activity).stop();
	Game.getInstance().stop();
    }

    public void pause(Activity activity)
    {
	AcceleratorListener.getInstance(activity).stop();
	Round.getInstance().pause();
    }

    public void resume(Activity activity)
    {
	AcceleratorListener.getInstance(activity).start();
	Round.getInstance().resume();
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

    public boolean isRoundRunning()
    {
	return Round.getInstance().isRunning();
    }

    public boolean isRoundPaused()
    {
	return Round.getInstance().isPaused();
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

	// Update position
	float x = (float) (ball.getPos().getX() + ball.getSpeed() * Math.sin(Math.toRadians(ball.getDirection())));
	float y = (float) (ball.getPos().getY() + ball.getSpeed() * Math.cos(Math.toRadians(ball.getDirection())));
	ball.setPos(new Vector2(x, y));

	// Check top border
	if (ball.getPos().getY() <= ball.getRadius())
	{
	    ball.setDirection((ball.getDirection() - 180) * -1);
	}

	// Check left and right border
	if ((ball.getPos().getX() <= ball.getRadius()) || (ball.getPos().getX() >= w - ball.getRadius()))
	{
	    ball.setDirection(360 - ball.getDirection());
	}

	// Check bottom border
	if (ball.getPos().getY() >= h - ball.getRadius())
	{
	    // Game.getInstance().stop();
	    Round.getInstance().stop();
	}

	// Check panel
	float panelWidth = Properties.getMinDimension() / 8;
	float panelHeight = Properties.getMinDimension() / 40;
	float ballX = ball.getPos().getX();
	float ballY = ball.getPos().getY();
	float panelX = panel.getPos().getX();

	float topEdge = Properties.getCanvasHeight() - panelHeight;
	float leftEdge = panelX - panelWidth / 2;
	float rightEdge = panelX + panelWidth / 2;

	// Check if ball hits panel
	if ((ballY + ball.getRadius() > topEdge) && (ballX > leftEdge) && (ballX < rightEdge))
	{
	    ball.setDirection(180 - (ballX - panelX) / panelWidth * 90);
	    // ball.setDirection((ball.getDirection() - 180) * -1);
	    ball.setSpeed(ball.getSpeed() + 0.005f);
	    Round.getInstance().incrementPoints();
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
	float x = Properties.getCanvasWidth() / 2 - (AcceleratorListener.getRawX() * Properties.getCanvasWidth() / 10);
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

    public static void readOuyaButton()
    {
	if (OuyaController.getControllerByDeviceId(0) != null)
	{
	    Log.debug("Controller found");

	    if (OuyaController.getControllerByDeviceId(0).buttonPressedThisFrame(OuyaController.BUTTON_O))
	    {
		Log.debug("Button O");
		buttonO = true;
	    }
	    if (OuyaController.getControllerByDeviceId(0).buttonPressedThisFrame(OuyaController.BUTTON_U))
	    {
		Log.debug("Button U");
		buttonU = true;
	    }
	    if (OuyaController.getControllerByDeviceId(0).buttonPressedThisFrame(OuyaController.BUTTON_Y))
	    {
		Log.debug("Button Y");
		buttonY = true;
	    }
	    if (OuyaController.getControllerByDeviceId(0).buttonPressedThisFrame(OuyaController.BUTTON_A))
	    {
		Log.debug("Button A");
		buttonA = true;
	    }
	}
    }

    public static boolean isButtonO()
    {
	return buttonO;
    }

    public static boolean isButtonU()
    {
	return buttonU;
    }

    public static boolean isButtonY()
    {
	return buttonY;
    }

    public static boolean isButtonA()
    {
	return buttonA;
    }
}
