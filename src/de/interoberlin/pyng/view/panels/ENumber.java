package de.interoberlin.pyng.view.panels;

import android.content.Context;
import android.content.res.Resources;
import de.interoberlin.pyng.R;
import de.interoberlin.pyng.controller.PyngController;

public enum ENumber
{

    ZERO(0, 0, 0, 0), 
    ONE(0, 0, 0, 0), 
    TWO(0, 0, 0, 0), 
    THREE(0, 0, 0, 0), 
    FOUR(0, 0, 0, 0), 
    FIVE(0, 0, 0, 0), 
    SIX(0, 0, 0, 0), 
    SEVEN(0, 0, 0, 0), 
    EIGHT(0, 0, 0, 0), 
    NINE(0, 0, 0, 0);

    private static Context c;
    private static Resources r = c.getResources();
    
    private static int w = (int) r.getDimension(R.dimen.numberWidth);
    private static int h = (int) r.getDimension(R.dimen.numberHeight);
    private static int l = (int) r.getDimension(R.dimen.numberLine);
    
    private int	    left;
    private int	    top;
    private int	    right;
    private int	    bottom;

    static
    {
	c = PyngController.getContext();
    }

    ENumber(int left, int top, int right, int bottom)
    {
	this.left = left;
	this.top = top;
	this.right = right;
	this.bottom = bottom;
    }

    public int getLeft()
    {
	return left;
    }

    public int getTop()
    {
	return top;
    }

    public int getRight()
    {
	return right;
    }

    public int getBottom()
    {
	return bottom;
    }
}
