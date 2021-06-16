package sampleclient;

import org.apache.log4j.Level;

public class LogLevel extends Level
{

	private static final long serialVersionUID = 1L;

	public static final LogLevel FINEST = new LogLevel(9800, "FINEST");

	public static final LogLevel FINER = new LogLevel(9900, "FINER");

	public static final LogLevel FINE = new LogLevel(10000, "FINE");


	private LogLevel(int i, String s)
	{
		super(i, s, 7);
	}

	
	public static Level toLevel(String s)
	{
		String s1 = s.toUpperCase();
		if (s1.equals("FINEST"))
		{
			return FINEST;
		}
		if (s1.equals("FINER"))
		{
			return FINER;
		}
		if (s1.equals("FINE"))
		{
			return FINE;
		} else
		{
			return Level.toLevel(s);
		}
	}


	public static Level toLevel(String s, Level level)
	{
		if (s == null)
		{
			return null;
		}
		String s1 = s.toUpperCase();
		if (s1.equals("FINEST"))
		{
			return FINEST;
		}
		if (s1.equals("FINER"))
		{
			return FINER;
		}
		if (s1.equals("FINE"))
		{
			return FINE;
		} else
		{
			return Level.toLevel(s, level);
		}
	}


	public static Level toLevel(int i)
	{
		return toLevel(i, INFO);
	}


	public static Level toLevel(int i, Level level)
	{
		switch (i)
		{
			case 9800:
				return FINEST;
			case 9900:
				return FINER;
			case 10000:
				return FINE;
		}
		return Level.toLevel(i, level);
	}
	
}
