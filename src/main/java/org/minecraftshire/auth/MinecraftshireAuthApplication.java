package org.minecraftshire.auth;

import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MinecraftshireAuthApplication {

	private static String SECRET_TOKEN;
	private static ConfigurableApplicationContext context;


	public static String getSecretToken() {
		return SECRET_TOKEN;
	}


	public static void stop() {
		context.stop();
	}


	public static void main(String[] args) {
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
		context = SpringApplication.run(MinecraftshireAuthApplication.class, args);
	}

}
