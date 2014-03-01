package de.interoberlin.pyng.model.geometry;

public class Vector2
{
    private float x;
    private float y;

    public Vector2(float x, float y)
    {
	this.x = x;
	this.y = y;
    }

    public float getX()
    {
	return x;
    }

    public void setX(float x)
    {
	this.x = x;
    }

    public float getY()
    {
	return y;
    }

    public void setY(float y)
    {
	this.y = y;
    }
    
    public int getDistance(Vector2 v)
    {
	return (int) Math.sqrt(Math.pow(this.getX()-v.getX(), 2)+Math.pow(this.getY()-v.getY(), 2));
    }
}
