package com.j2js.test;

public class Timeout {
    
    private static int count = 0;
    
    public final long initTime = System.currentTimeMillis();
    public final long failAfter;
    public final String description;
    public final String id = String.valueOf(count++);
    
    public Timeout(String description, long failAfter) {
        this.description = description;
        this.failAfter = failAfter;
    }
}
