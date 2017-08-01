package org.minecraftshire.auth.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessRunner {

    public static long getPid() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }


    public static List<String> outList(String... command) throws IOException {
        ProcessBuilder ps = new ProcessBuilder(command);
        ps.redirectErrorStream(true);

        Process pr = ps.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        try {
            pr.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return lines;
    }


    public static String out(String... command) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> lines = ProcessRunner.outList(command);

        for (String line: lines) {
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }


    public static String exec(String... command) {
        try {
            return ProcessRunner.out(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
