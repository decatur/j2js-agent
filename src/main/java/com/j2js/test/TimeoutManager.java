package com.j2js.test;

public interface TimeoutManager {
    Timeout failAfter(String description, long millis);
    void defuse(Timeout token);
}
