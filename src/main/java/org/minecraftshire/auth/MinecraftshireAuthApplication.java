package org.minecraftshire.auth;

import org.apache.commons.cli.*;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class MinecraftshireAuthApplication {

	private static String secretToken;
	private static String buildNumber;
	private static String path;
	private static Logger log = LoggerFactory.getLogger(MinecraftshireAuthApplication.class);


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

		Options options = new Options();
		options.addOption(secret);
		options.addOption(path);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			log.error("MinecraftshireAuthApplication", e);

			System.exit(-1);
			return;
		}

		MinecraftshireAuthApplication.secretToken = cmd.getOptionValue("secret");
		MinecraftshireAuthApplication.path = cmd.getOptionValue("path");
		MinecraftshireAuthApplication.buildNumber = ProcessRunner.exec(
				"cd " + MinecraftshireAuthApplication.path + "/ && git rev-list HEAD --count"
		);
	}


	private static void writePid() {
		PrintWriter out;

		try {
			out = new PrintWriter(path + "/server.pid");
		} catch (FileNotFoundException e) {
			log.error("MinecraftshireAuthApplication", e);
			return;
		}

		out.println(ProcessRunner.getPid());
		out.close();
	}


	public static void main(String[] args) {
		loadArgs(args);
		writePid();

		SpringApplication.run(MinecraftshireAuthApplication.class, args);
	}

}
