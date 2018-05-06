package com.example.clabuyakchai.brushtrainingsimulator.model;

/**
 * Created by Clabuyakchai on 06.05.2018.
 */

public class UserStatistics {
    private Integer counter;
    private Long data;
    private String username;

    public UserStatistics() {
    }

    public UserStatistics(Integer counter, Long data, String username) {
        this.counter = counter;
        this.data = data;
        this.username = username;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
