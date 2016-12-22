package com.example.katesudal.spikefirebase.Model;

import java.util.List;

/**
 * Created by katesuda.l on 15/12/2559.
 */

public class SendNotificationResponse {
    private int multicast_id;
    private int success;
    private int failure;
    private int canonical_ids;
    private List<Result> results;

    public SendNotificationResponse(){}

    public SendNotificationResponse(int canonical_ids, int failure, int multicast_id, List<Result> results, int success) {
        this.canonical_ids = canonical_ids;
        this.failure = failure;
        this.multicast_id = multicast_id;
        this.results = results;
        this.success = success;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(int multicast_id) {
        this.multicast_id = multicast_id;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
