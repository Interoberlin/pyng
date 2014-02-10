package de.interoberlin.pyng.model.objects;

import java.util.ArrayList;
import java.util.List;

import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;
import de.interoberlin.pyng.model.geometry.Vector2;

public class Panel
{
    private List<Vector2> posList;

    private final float   width;
    private final float   height;

    private final int     MAX_LIST_SIZE = 10;

    public Panel()
    {
	this(new Vector2(0, 0));
    }

    public Panel(Vector2 pos)
    {
	posList = new ArrayList<Vector2>();
	posList.add(pos);
	this.width = PyngController.getContext().getResources().getDimension(R.dimen.panelWidth);
	this.height = PyngController.getContext().getResources().getDimension(R.dimen.panelHeight);
    }

    public Vector2 getPos()
    {
	List<Vector2> currentPosList = new ArrayList<Vector2>(posList);
	Vector2 currentPos = new Vector2(0, 0);

	for (Vector2 p : currentPosList)
	{
	    if (p != null)
	    {
		currentPos = new Vector2(currentPos.getX() + p.getX() / posList.size(), currentPos.getY() + p.getY() / posList.size());
	    }
	}

	return currentPos;
    }

    public void setPos(Vector2 pos)
    {
	posList.add(pos);
	if (posList.size() > MAX_LIST_SIZE)
	{
	    posList.remove(0);
	}
    }

    public float getWidth()
    {
	return width;
    }

    public float getHeight()
    {
	return height;
    }
}