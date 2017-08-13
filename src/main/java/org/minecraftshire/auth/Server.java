package org.minecraftshire.auth;

import org.apache.commons.cli.*;
import org.minecraftshire.auth.aspects.AuthAspect;
import org.minecraftshire.auth.aspects.UserAgent;
import org.minecraftshire.auth.controllers.AuthController;
import org.minecraftshire.auth.repositories.TokenRepository;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.minecraftshire.auth.utils.logging.FileLogWriter;
import org.minecraftshire.auth.utils.logging.Logger;
import org.minecraftshire.auth.utils.logging.StdOutLogWriter;
import org.minecraftshire.auth.utils.logging.SystemRedirectStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


@SpringBootApplication
public class Server {

	private static String secretToken;
	private static String buildNumber;
	private static String buildDate;
	private static String path;
	private static String logPath;
	private static String geoDbVersion;
	private static Logger log = Logger.getLogger();
	private static SystemRedirectStream redirectStream;
	private static Environment env;
	private static ConfigurableApplicationContext context;



	public static String getSecretToken() {
		return secretToken;
	}

	public static String getBuildNumber() {
		return buildNumber;
	}

	public static String getPath() {
		return path;
	}

	public static String getBuildDate() {
		return buildDate;
	}

	public static Environment getEnv() {
		return env;
	}

	public static String getIssuer() {
		return env.getProperty("minecraftshire.name") + " " +
				env.getProperty("minecraftshire.version");
	}

	public static String getGeoDbVersion() {
		return geoDbVersion;
	}

	public static ConfigurableApplicationContext getContext() {
		return context;
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

		Server.secretToken = cmd.getOptionValue("secret");
		Server.path = cmd.getOptionValue("path");
		Server.buildNumber = ProcessRunner.exec(
				"cd " + Server.path + "/ && git rev-list HEAD --count"
		);
		Server.buildDate = ProcessRunner.exec(
				"cd " + Server.path + "/ && git log -1 --format=%cd "
		);

		Server.logPath = cmd.getOptionValue("log", null);

		try {
			Server.geoDbVersion = new String(Files.readAllBytes(Paths.get(
                    Server.getPath(), "assets", "geo-db", "version.info"
            )), Charset.defaultCharset()).replace("./", "").replace("\n", "");
		} catch (IOException e) {
			log.severe(e);

			System.exit(-1);
			return;
		}
	}


	private static void initLogger() {
		String fileName = Server.logPath;

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
		redirectStream.setVerbose(false);
		log = Logger.getLogger();

		log.info("Successfully switched to file \"", fileName ,"\"");
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
		/*Logger.setLogger(new Logger(new StdOutLogWriter()));

		AuthController auth = new AuthController(null, null);
		Method method = AuthAspect.firstMethod(auth, "history");

		Logger.getLogger().info("LENGTH: ", method.getParameters().length);

		for (Parameter parameter: method.getParameters()) {
			Logger.getLogger().info("PARAMETER ", parameter.getName(), ": ",
					parameter.getAnnotations().length);

			for (Annotation annotation: parameter.getAnnotations()) {
				Logger.getLogger().info("\tANNOTATION ",
						annotation.annotationType().isAssignableFrom(UserAgent.class));
			}
		}*/

		loadArgs(args);
		initLogger();
		writePid();

		context = SpringApplication.run(Server.class, args);
		env = context.getEnvironment();

		// Truncate login history older than 1 year
		context.getBean(TokenRepository.class).truncateHistory();
	}

}
