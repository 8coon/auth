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

	private static String SECRET_TOKEN;
	private static String buildNumber;


	public static String getSecretToken() {
		return SECRET_TOKEN;
	}

	public static String getBuildNumber() {
		return buildNumber;
	}


	public static void stop() {
		System.err.println("Stopping via System.exit(0)...");
		System.exit(0);
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
		buildNumber = ProcessRunner.exec("git", "rev-list", "HEAD" , "--count").replace("\n", "");
	}


	private static void writePid() {
		PrintWriter out;

		try {
			out = new PrintWriter("server.pid");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		out.println(ProcessRunner.getPid());
		out.close();
	}


	public static void main(String[] args) {
		writePid();
		loadArgs(args);
		loadMetaInf();

		SpringApplication.run(MinecraftshireAuthApplication.class, args);
	}

}
