package garioUtil;

public class ErrorLog
{
	//my Rather Unique Naming Style XD! please Do not criticize only NASM Bros would understand (java losers !)
	private static StringBuilder __logString__ = new StringBuilder();
	public static boolean __logErrors__ = true;
	
	
	public static void logError(String errorMsg)
	{
		if(__logErrors__)
		{
			__logString__.append("[error!!] - ");
			__logString__.append(errorMsg);
			__logString__.append("\n");
		}
	}
	
	public static void logWarn(String warnMsg)
	{
		if(__logErrors__)
		{
			__logString__.append("[big warning!!] - ");
			__logString__.append(warnMsg);
			__logString__.append("\n");
		}
	}
	
	public static void flushLog()
	{
		__logString__ = new StringBuilder();
	}

	public static void flushAndPrint()
	{
		if(__logString__.length() > 0)
		{
			System.out.println(__logString__.toString());
			flushLog();
		}
	}
	
	public static String getErrorLog()
	{
		return __logString__.toString();
	}
}
