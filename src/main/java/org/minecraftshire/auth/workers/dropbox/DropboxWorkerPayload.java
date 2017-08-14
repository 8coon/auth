package org.minecraftshire.auth.workers.dropbox;


import org.springframework.jdbc.core.JdbcTemplate;


public class DropboxWorkerPayload {

    private JdbcTemplate jdbc;
    private int id;
    private String localFileName;


    /**
     * For JdbcTemplate's thread safety see:
     * https://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html#jdbc-JdbcTemplate-idioms
     */
    public DropboxWorkerPayload(JdbcTemplate jdbc, int id, String localFileName) {
        this.jdbc = jdbc;
        this.id = id;
        this.localFileName = localFileName;
    }


    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    public int getId() {
        return id;
    }

    public String getLocalFileName() {
        return localFileName;
    }

}
