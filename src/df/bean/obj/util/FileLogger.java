package df.bean.obj.util;

import org.apache.log4j.Logger;

public class FileLogger {
	static Logger log = Logger.getLogger(FileLogger.class.getName());

	public static void writeLogInfo(String logMessage){
		log.info(logMessage);
	}
}