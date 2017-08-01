package org.minecraftshire.auth;

import org.apache.commons.cli.*;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.util.Properties;


@SpringBootApplication
public class MinecraftshireAuthApplication {

	private static String secretToken;
	private static String buildNumber;
	private static String path;


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
		System.err.println("Stopping via System.exit(0)...");
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
			System.err.println(e.getMessage());

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
		//cd #{JAVA_PATH}/ && git rev-list HEAD --count

		try {
			out = new PrintWriter(path + "/server.pid");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
