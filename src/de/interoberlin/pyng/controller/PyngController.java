package de.interoberlin.pyng.controller;

import tv.ouya.console.api.OuyaController;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.SparseBooleanArray;
import de.interoberlin.pyng.controller.accelerometer.AcceleratorListener;
import de.interoberlin.pyng.controller.game.Game;
import de.interoberlin.pyng.controller.game.Round;
import de.interoberlin.pyng.model.geometry.RandomNumber;
import de.interoberlin.pyng.model.geometry.Vector2;
import de.interoberlin.pyng.model.objects.Ball;
import de.interoberlin.pyng.model.objects.Panel;
import de.interoberlin.pyng.model.settings.Properties;
import de.interoberlin.pyng.model.sound.ESound;
import de.interoberlin.pyng.view.activities.PyngActivity;

public class PyngController extends Application
{

    private static Ball	       ball;
    private static Panel	      panel;
    private static boolean	    initialized = false;

    private static SparseBooleanArray buttons;

    static
    {
	buttons = new SparseBooleanArray();
	buttons.append(OuyaController.BUTTON_O, false);
	buttons.append(OuyaController.BUTTON_U, false);
	buttons.append(OuyaController.BUTTON_Y, false);
	buttons.append(OuyaController.BUTTON_A, false);

	buttons.append(OuyaController.BUTTON_DPAD_DOWN, false);
	buttons.append(OuyaController.BUTTON_DPAD_LEFT, false);
	buttons.append(OuyaController.BUTTON_DPAD_UP, false);
	buttons.append(OuyaController.BUTTON_DPAD_RIGHT, false);

	buttons.append(OuyaController.BUTTON_R1, false);
	buttons.append(OuyaController.BUTTON_L1, false);
    }

    private static Context	    context;

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
	    PyngActivity.playSound(ESound.BONG);
	    ball.setDirection((ball.getDirection() - 180) * -1);
	}

	// Check left and right border
	if ((ball.getPos().getX() <= ball.getRadius()) || (ball.getPos().getX() >= w - ball.getRadius()))
	{
	    PyngActivity.playSound(ESound.BONG);
	    ball.setDirection(360 - ball.getDirection());
	}

	// Check bottom border
	if (ball.getPos().getY() >= h - ball.getRadius())
	{
	    PyngActivity.playSound(ESound.GAME_OVER);
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
	    PyngActivity.playSound(ESound.BING);
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

    public static void indicateKey(int keyCode, boolean pressed)
    {
	buttons.put(keyCode, pressed);
    }

    public static boolean isKeyPressed(int keyCode)
    {
	return buttons.get(keyCode, false);
    }
}
