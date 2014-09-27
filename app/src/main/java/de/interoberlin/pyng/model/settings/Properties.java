package de.interoberlin.pyng.model.settings;

public class Properties
{
    private static int canvasWidth;
    private static int canvasHeight;

    public static int getCanvasWidth()
    {
	return canvasWidth;
    }

    public static void setCanvasWidth(int canvasWidth)
    {
	Properties.canvasWidth = canvasWidth;
    }

    public static int getCanvasHeight()
    {
	return canvasHeight;
    }

    public static void setCanvasHeight(int canvasHeight)
    {
	Properties.canvasHeight = canvasHeight;
    }

    public static int getMinDimension()
    {
	return canvasHeight > canvasWidth ? canvasWidth : canvasHeight;
    }
}
