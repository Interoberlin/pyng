package de.interoberlin.pyng.controller.log;

import java.util.Calendar;

public class LogEntry
{
	private String	timeStamp;
	private ELog	logLevel;
	private String	message;

	public LogEntry(ELog logLevel, String message)
	{
		String hours = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		String minutes = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
		String seconds = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
		String millis = String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND));

		
		
		this.timeStamp = addLeadingZeros(hours,2) + ":" + addLeadingZeros(minutes,2) + ":" + addLeadingZeros(seconds,2) + "." + addLeadingZeros(millis,3);
		this.logLevel = logLevel;
		this.message = message;
	}

	private String addLeadingZeros(String s, int digits)
	{
		while (s.length() < digits)
		{
			s = "0".concat(s);
		}

		return s;
	}

	public String getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public ELog getLogLevel()
	{
		return logLevel;
	}

	public void setLogLevel(ELog logLevel)
	{
		this.logLevel = logLevel;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
