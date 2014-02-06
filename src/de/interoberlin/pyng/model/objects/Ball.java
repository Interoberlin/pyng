package de.interoberlin.pyng.model.objects;

import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.model.geometry.Vector2;

public class Ball
{
    private Vector2 pos;
    private float   direction;
    private float   speed;

    private int     radius;

    public Ball()
    {
	this(new Vector2(10, 10), 0, 0);
    }

    public Ball(Vector2 pos, float direction, float speed)
    {
	this.pos = pos;
	this.direction = direction;
	this.speed = speed;
	this.radius = (int) PyngController.getContext().getResources().getDimension(R.dimen.ballRadius);
    }

    public Vector2 getPos()
    {
	return pos;
    }

    public void setPos(Vector2 pos)
    {
	this.pos = pos;
    }

    public float getDirection()
    {
	return direction;
    }

    public void setDirection(float direction)
    {
	this.direction = direction;
    }

    public float getSpeed()
    {
	return speed;
    }

    public void setSpeed(float speed)
    {
	this.speed = speed;
    }

    public int getRadius()
    {
	return radius;
    }
}