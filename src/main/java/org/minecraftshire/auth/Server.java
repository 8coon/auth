package org.minecraftshire.auth;

import org.apache.commons.cli.*;
import org.minecraftshire.auth.repositories.TokenRepository;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.minecraftshire.auth.utils.logging.FileLogWriter;
import org.minecraftshire.auth.utils.logging.Logger;
import org.minecraftshire.auth.utils.logging.SystemRedirectStream;
import org.minecraftshire.auth.workers.dropbox.DropboxWorker;
import org.minecraftshire.auth.workers.dropbox.DropboxWorkerPayload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
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
	private static String uploadsPath;
	private static String geoDbVersion;
	private static Logger log = Logger.getLogger();
	private static SystemRedirectStream redirectStream;
	private static Environment env;
	private static ConfigurableApplicationContext context;
	private static DropboxWorker dropboxWorker;



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

	public static String getUploadsPath() {
		return uploadsPath;
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
		Server.uploadsPath = Server.path + "/assets/uploads";

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
		loadArgs(args);
		initLogger();
		writePid();

		context = SpringApplication.run(Server.class, args);
		env = context.getEnvironment();

		dropboxWorker = new DropboxWorker();

		for (int i = 0; i < 1000; i++) {
			dropboxWorker.schedule(new DropboxWorkerPayload(
					(JdbcTemplate) context.getBean(JdbcTemplate.class), 0, ""
			));
		}

		// Truncate login history older than 1 year
		context.getBean(TokenRepository.class).truncateHistory();
	}

}
