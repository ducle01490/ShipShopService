package com.shipper.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

@SuppressWarnings("deprecation")
public class LoggerUtil {
	private static final String folder_logs = "./logs/";
	private static String rootName;
	private static Logger rootLogger;

	public static Logger getDailyLogger(String loggerName, String outputFile) {
		if (Logger.exists(loggerName) != null) {
			return Logger.getLogger(loggerName);
		}
		try {
			
			File file = new File(outputFile+loggerName);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			Properties properties = new Properties();
			properties.put(new StringBuilder().append("log4j.logger.").append(loggerName).toString(),
					new StringBuilder().append("INFO, ").append(loggerName).toString());
			properties.put(new StringBuilder().append("log4j.additivity.").append(loggerName).toString(), "false");
			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName).toString(),
					"org.apache.log4j.DailyRollingFileAppender");

			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName).append(".File").toString(),
					new StringBuilder().append(file.getAbsolutePath()).toString());

			properties.put(
					new StringBuilder().append("log4j.appender.").append(loggerName).append(".DatePattern").toString(),
					"'.'yyyy-MM-dd");

			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName).append(".ImmediateFlush")
					.toString(), "true");

			properties.put(
					new StringBuilder().append("log4j.appender.").append(loggerName).append(".layout").toString(),
					"org.apache.log4j.PatternLayout");

			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName)
					.append(".layout.ConversionPattern").toString(),
					"%d{dd MMM yyyy HH:mm:ss.SSS} [%t] %-5p %c  - \n  %m%n \n");

			PropertyConfigurator.configure(properties);
			return LogManager.getLogger(loggerName);
		} catch (Exception e) {
		}
		return Logger.getRootLogger();
	}

	
	public static Logger getDailyLogger(String loggerName) {
		if (Logger.exists(loggerName) != null) {
			return Logger.getLogger(loggerName);
		}

		try {
			Properties properties = new Properties();
			properties.put(new StringBuilder().append("log4j.logger.").append(loggerName).toString(),
					new StringBuilder().append("INFO, ").append(loggerName).toString());
			properties.put(new StringBuilder().append("log4j.additivity.").append(loggerName).toString(), "false");
			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName).toString(),
					"org.apache.log4j.DailyRollingFileAppender \n");

			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName).append(".File").toString(),
					new StringBuilder().append(folder_logs).append(loggerName).append(".txt").toString());

			properties.put(
					new StringBuilder().append("log4j.appender.").append(loggerName).append(".DatePattern").toString(),
					"'.'yyyy-MM-dd");

			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName).append(".ImmediateFlush")
					.toString(), "true");

			properties.put(
					new StringBuilder().append("log4j.appender.").append(loggerName).append(".layout").toString(),
					"org.apache.log4j.PatternLayout");

			properties.put(new StringBuilder().append("log4j.appender.").append(loggerName)
					.append(".layout.ConversionPattern").toString(),
					"%d{dd MMM yyyy HH:mm:ss.SSS} [%t] %-5p %c  - \n  %m%n \n");

			PropertyConfigurator.configure(properties);
			return LogManager.getLogger(loggerName);
		} catch (Exception e) {
		}
		return Logger.getRootLogger();
	}

	public static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
		} catch (IOException e) {
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
				}
		}
		return sb.toString();
	}

	public static void createRootLogger(String name) {
		if (Logger.exists(name) == null) {
			rootName = name;
			rootLogger = getDailyLogger(name);
		}
	}

	public static void createRootLogger(String name, String dir) {
		if (Logger.exists(name) == null) {
			rootName = name;
			rootLogger = getDailyLogger(name, dir);
		}
	}

	public static Logger getRootLogger() {
		if (rootLogger != null)
			return rootLogger;
		else {
			if (rootName != null) {
				rootLogger = getDailyLogger(rootName);
				return rootLogger;
			}
		}
		return Logger.getRootLogger();
	}

	public static String normalizeMarkUpperCase(String text) {
		return Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
				.matcher(Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD)).replaceAll("").replace('d', 'd')
				.trim();
	}

	public static void main(String[] args) {
		System.out.println(normalizeMarkUpperCase(
				"Tâm điểm của Premier League cuối tuần này sẽ là trận “derby nước Anh” trên sân Old Trafford giữa hai thế lực cùng mang sắc đỏ của xứ sở sương mù là Manchester United và Liverpool. Bên cạnh đó, cả Man City, Chelsea và Arsenal cũng sẽ gặp những đối thủ không hề dễ chơi."));
	}
}