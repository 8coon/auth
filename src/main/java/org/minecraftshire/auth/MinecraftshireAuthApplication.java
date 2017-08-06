package org.minecraftshire.auth;

import org.apache.commons.cli.*;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.minecraftshire.auth.utils.logging.FileLogWriter;
import org.minecraftshire.auth.utils.logging.Logger;
import org.minecraftshire.auth.utils.logging.StdOutLogWriter;
import org.minecraftshire.auth.utils.logging.SystemRedirectStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;


@SpringBootApplication
public class MinecraftshireAuthApplication {

	private static String secretToken;
	private static String buildNumber;
	private static String path;
	private static String logPath;
	private static Logger log = Logger.getLogger();
	private static SystemRedirectStream redirectStream;



	public static String getSecretToken() {
		return secretToken;
	}

	public static String getBuildNumber() {
		return buildNumber;
	}

	public static String getPath() {
		return path;
	}


	public static void stop() {
		log.info("Stopping via System.exit(0)...");
		System.exit(0);
	}


	private static void loadArgs(String[] args) {
		Option secret = new Option("s", "secret", true,
				"Secret token for administrative methods");
		secret.setRequired(true);

		Option path = new Option("p", "path", true, "Server root path");
		path.setRequired(true);

		Option logPath = new Option("l", "log", true, "Log file path");

		Options options = new Options();
		options.addOption(secret);
		options.addOption(path);
		options.addOption(logPath);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			log.severe(e);

			System.exit(-1);
			return;
		}

		MinecraftshireAuthApplication.secretToken = cmd.getOptionValue("secret");
		MinecraftshireAuthApplication.path = cmd.getOptionValue("path");
		MinecraftshireAuthApplication.buildNumber = ProcessRunner.exec(
				"cd " + MinecraftshireAuthApplication.path + "/ && git rev-list HEAD --count"
		);

		MinecraftshireAuthApplication.logPath = cmd.getOptionValue("log", null);
	}


	private static void initLogger() {
		String fileName = MinecraftshireAuthApplication.logPath;

		if (fileName == null) {
			log.info("Logging into System.err");
			return;
		}

		log.info("Switching to logging into file \"" + fileName + "\"...");

		try {
			Logger.setLogger(new Logger(new FileLogWriter(fileName)));
		} catch (FileNotFoundException e) {
			log.severe(e);

			System.exit(-1);
			return;
		}

		redirectStream = new SystemRedirectStream(Logger.getLogger(), System.out, System.err);
		redirectStream.setVerbose(true);
		log = Logger.getLogger();

		log.info("Switched to file \"", fileName ,"\" successfully");
	}


	private static void writePid() {
		PrintWriter out;

		try {
			out = new PrintWriter(path + "/server.pid");
		} catch (FileNotFoundException e) {
			log.severe(e);
			return;
		}

		out.println(ProcessRunner.getPid());
		out.close();
	}


	public static void main(String[] args) {
		loadArgs(args);
		initLogger();
		writePid();

		SpringApplication.run(MinecraftshireAuthApplication.class, args);
	}

}
