package org.minecraftshire.auth;

import org.apache.commons.cli.*;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


@SpringBootApplication
public class MinecraftshireAuthApplication {

	private static String SECRET_TOKEN;
	private static ConfigurableApplicationContext context;
	private static String name;
	private static String description;
	private static String version;


	public static String getSecretToken() {
		return SECRET_TOKEN;
	}

	public static String getName() {
		return name;
	}

	public static String getDescription() {
		return description;
	}

	public static String getVersion() {
		return version;
	}


	public static void stop() {
		// context.stop();
		System.err.println("Stopping via System.exit(0)...");
		System.exit(0);
	}


	private static String getBuildNumber() {
		return ProcessRunner.exec("git", "rev-list", "HEAD" , "--count").replace("\n", "");
	}


	private static void loadArgs(String[] args) {
		Option secret = new Option("s", "secret", true,
				"Secret token for administrative methods");
		secret.setRequired(true);

		Options options = new Options();
		options.addOption(secret);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());

			System.exit(-1);
			return;
		}

		SECRET_TOKEN = cmd.getOptionValue("secret");
	}


	private static void loadMetaInf() {
		Properties properties = new Properties();

		try {
			properties.load(context.getResource("application.properties").getInputStream());
		} catch (IOException e) {
			e.printStackTrace();

			System.exit(-1);
			return;
		}

		name = properties.getProperty("minecraftshire.name");
		description = properties.getProperty("minecraftshire.description");
		version = properties.getProperty("minecraftshire.version") + "." + getBuildNumber();
	}


	public static void main(String[] args) {
		loadArgs(args);

		context = SpringApplication.run(MinecraftshireAuthApplication.class, args);
		loadMetaInf();
	}

}
