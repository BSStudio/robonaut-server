package hu.bsstudio.robonaut.safetycar.model;

import lombok.Value;

@Value
public class SafetyCarFollowInformation {
    long teamId;
    boolean safetyCarFollowed;
}
