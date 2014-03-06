package de.interoberlin.pyng.model.objects;

import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.model.geometry.Vector2;

public class Panel
{
    private Vector2     pos;

    private final float width;
    private final float height;
    private final float speed;

    public Panel()
    {
	this(new Vector2(0, 0), 0);
    }

    public Panel(Vector2 pos, float speed)
    {
	this.pos = pos;
	this.width = PyngController.getContext().getResources().getDimension(R.dimen.panelWidth);
	this.height = PyngController.getContext().getResources().getDimension(R.dimen.panelHeight);
	this.speed = 0;
    }

    public Vector2 getPos()
    {
	return pos;
    }

    public void setPos(Vector2 pos)
    {
	this.pos = pos;
    }

    public float getWidth()
    {
	return width;
    }

    public float getHeight()
    {
	return height;
    }

    public float getSpeed()
    {
	return speed;
    }
}