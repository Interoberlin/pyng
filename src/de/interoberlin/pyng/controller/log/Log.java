package de.interoberlin.pyng.controller.log;

import java.util.ArrayList;
import java.util.List;

import de.interoberlin.pyng.view.activities.PyngActivity;

public class Log
{
    private static List<LogEntry> log    = new ArrayList<LogEntry>();
    private static int	    buffer = 100;

    public Log()
    {

    }

    public static void add(LogEntry logEntry)
    {
	log.add(logEntry);
    }

    private static void add(ELog logLevel, String message)
    {
	while (log.size() > buffer)
	{
	    removeLast();
	}

	log.add(new LogEntry(logLevel, message));
	PyngActivity.uiDraw();
    }

    public static void trace(String message)
    {
	add(ELog.TRACE, message);
    }

    public static void debug(String message)
    {
	add(ELog.DEBUG, message);
    }

    public static void info(String message)
    {
	add(ELog.INFO, message);
    }

    public static void warn(String message)
    {
	add(ELog.WARN, message);
    }

    public static void error(String message)
    {
	add(ELog.ERROR, message);
    }

    public static void fatal(String message)
    {
	add(ELog.FATAL, message);
    }

    public static List<LogEntry> getAll()
    {
	return log;
    }

    public static void clear()
    {
	log.clear();
    }

    public static void removeLast()
    {
	log.remove(0);
    }
}
