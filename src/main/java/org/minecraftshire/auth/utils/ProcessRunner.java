package org.minecraftshire.auth.utils;


import org.minecraftshire.auth.utils.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ProcessRunner {

    private static Logger log = Logger.getLogger();


    public static long getPid() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }


    public static List<String> outList(String command) throws IOException {
        Process pr = Runtime.getRuntime().exec(new String[] {"sh", "-c", command});

        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        try {
            pr.waitFor();
        } catch (InterruptedException e) {
            log.warning(e);
        }

        return lines;
    }


    public static String out(String command) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> lines = ProcessRunner.outList(command);
        int i = 0;

        for (String line: lines) {
            sb.append(line);
            i++;

            if (i != lines.size()) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }


    public static String exec(String command) {
        try {
            return ProcessRunner.out(command);
        } catch (IOException e) {
            log.severe(e);
        }

        return "";
    }

}
