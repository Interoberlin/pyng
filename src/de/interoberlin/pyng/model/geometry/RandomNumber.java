package de.interoberlin.pyng.model.geometry;

import java.util.Random;

public class RandomNumber
{
    public static int getRandomNumber(int min, int max)
    {
	return new Random().nextInt((max - min) + 1) + min;
    }
}
