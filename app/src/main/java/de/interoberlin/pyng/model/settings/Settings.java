package de.interoberlin.pyng.model.settings;

public class Settings
{
    // Value range to transmit
    private static final float MAX_VALUE	   = 10F;
    private static final float MIN_VALUE	   = -10F;

    // Sensitivity factors
    private static final float MIN_FACTOR	  = 1F;
    private static final float MAX_FACTOR	  = 4F;

    // Settings activity
    private static boolean     debug	       = true;
    private static boolean     post		= true;
    private static boolean     get		 = false;
    private static String      url		 = "192.168.1.1";
    private static int	 frequency	   = 1;
    private static int	 logBuffer	   = 1000;
    private static int	 stabilizationBuffer = 10;
    private static int	 sensitivityX	= 50;
    private static int	 sensitivityY	= 50;

    // Display
    private static int	 circleCount	 = 4;
    private static int	 circleLineWidth     = Properties.getCanvasWidth() / 72;
    private static int	 pointRadius	 = Properties.getCanvasWidth() / 180;

    // Offset
    private static float       offsetX	     = 0F;
    private static float       offsetY	     = 0F;

    public static float getMaxValue()
    {
	return MAX_VALUE;
    }

    public static float getMinValue()
    {
	return MIN_VALUE;
    }

    public static float getMinFactor()
    {
	return MIN_FACTOR;
    }

    public static float getMaxFactor()
    {
	return MAX_FACTOR;
    }

    public static boolean isDebug()
    {
	return debug;
    }

    public static void setDebug(boolean debug)
    {
	Settings.debug = debug;
    }

    public static void toggleDebug()
    {
	if (isDebug())
	{
	    setDebug(false);
	} else
	{
	    setDebug(true);
	}
    }

    public static boolean isPost()
    {
	return post;
    }

    public static void setPost(boolean post)
    {
	Settings.post = post;
    }

    public static boolean isGet()
    {
	return get;
    }

    public static void setGet(boolean get)
    {
	Settings.get = get;
    }

    public static String getUrl()
    {
	return url;
    }

    public static void setUrl(String url)
    {
	Settings.url = url;
    }

    public static int getFrequency()
    {
	return frequency;
    }

    public static void setFrequency(int frequency)
    {
	Settings.frequency = frequency;
    }

    public static int getLogBuffer()
    {
	return logBuffer;
    }

    public static void setLogBuffer(int logBuffer)
    {
	Settings.logBuffer = logBuffer;
    }

    public static int getStabilizationBuffer()
    {
	return stabilizationBuffer;
    }

    public static void setStabilizationBuffer(int stabilizationBuffer)
    {
	Settings.stabilizationBuffer = stabilizationBuffer;
    }

    public static int getSensitivityX()
    {
	return sensitivityX;
    }

    public static void setSensitivityX(int sensitivityX)
    {
	Settings.sensitivityX = sensitivityX;
    }

    public static int getSensitivityY()
    {
	return sensitivityY;
    }

    public static void setSensitivityY(int sensitivityY)
    {
	Settings.sensitivityY = sensitivityY;
    }

    public static int getCircleCount()
    {
	return circleCount;
    }

    public static void setCircleCount(int circleCount)
    {
	Settings.circleCount = circleCount;
    }

    public static int getCircleLineWidth()
    {
	return circleLineWidth;
    }

    public static void setCircleLineWidth(int circleLineWidth)
    {
	Settings.circleLineWidth = circleLineWidth;
    }

    public static int getPointRadius()
    {
	return pointRadius;
    }

    public static void setPointRadius(int pointRadius)
    {
	Settings.pointRadius = pointRadius;
    }

    public static float getOffsetX()
    {
	return offsetX;
    }

    public static void setOffsetX(float offsetX)
    {
	Settings.offsetX = offsetX;
    }

    public static float getOffsetY()
    {
	return offsetY;
    }

    public static void setOffsetY(float offsetY)
    {
	Settings.offsetY = offsetY;
    }
}
