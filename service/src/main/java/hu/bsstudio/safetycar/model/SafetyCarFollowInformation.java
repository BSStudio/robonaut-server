package hu.bsstudio.safetycar.model;

import lombok.Value;

@Value
public class SafetyCarFollowInformation {
    long teamId;
    boolean safetyCarFollowed;
}
