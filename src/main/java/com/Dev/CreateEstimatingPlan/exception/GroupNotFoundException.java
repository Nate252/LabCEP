package com.Dev.CreateEstimatingPlan.exception;

public class GroupNotFoundException extends RuntimeException {

    public GroupNotFoundException(String name) {
        super(name);
    }
}
