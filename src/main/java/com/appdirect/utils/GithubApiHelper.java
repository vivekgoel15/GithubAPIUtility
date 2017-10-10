package com.appdirect.utils;

import com.appdirect.configuration.Constants;
import com.appdirect.enums.RequestType;
import com.appdirect.rest.RestHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by vigoel on 13/6/16.
 */
public class GithubApiHelper {

    private static final Logger LOGGER = Logger.getLogger(GithubApiHelper.class);

    private static RestHandler restHandler;

    public static void setRestHandler(RestHandler restHandler) {
        GithubApiHelper.restHandler = restHandler;
    }

    public static void processPRs() {
        // Loads the PR changes file and creates pull requests change maps
        try {
            FileUtils.init();
        } catch (IOException e) {
            LOGGER.error("Error while reading PR changes file", e);
            return;
        }
        //Remove assignees
        removePRAssignees();
        //Add assignees
        addPRAssignees();
        //Remove labels
        removePRLabels();
        //Add assignees
        addPRLabels();
        //Add comments
        addPRComments();
    }

    /**
     * Removes labels from the pull request.
     */
    public static void removePRAssignees() {
        LOGGER.info("============Remove Assignees============");
        Map<String, String> prAssigneeMap = FileUtils.getPullRequestsChangeMap(RequestType.REMOVE_ASSIGNEE);
        Iterator it1 = prAssigneeMap.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry pr = (Map.Entry) it1.next();
            removePRAssignee((String) pr.getKey(), (String) pr.getValue());
        }
    }

    /**
     * Add all the pull requests assignees.
     */
    public static void addPRAssignees() {
        LOGGER.info("============Add Assignees============");
        Map<String, String> prAssigneeMap = FileUtils.getPullRequestsChangeMap(RequestType.ADD_ASSIGNEE);
        Iterator it = prAssigneeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pr = (Map.Entry) it.next();
            addPRAssignee((String) pr.getKey(), (String) pr.getValue());
        }
    }

    /**
     * Removes labels from the pull request.
     */
    public static void removePRLabels() {
        LOGGER.info("============Remove Labels============");
        Map<String, String> prLabelsMap = FileUtils.getPullRequestsChangeMap(RequestType.REMOVE_LABEL);
        Iterator it1 = prLabelsMap.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry pr = (Map.Entry) it1.next();
            removePRLabel((String) pr.getKey(), (String) pr.getValue());
        }
    }

    /**
     * Add labels to the pull request.
     */
    public static void addPRLabels() {
        LOGGER.info("============Add Labels============");
        Map<String, String> prLabelsMap = FileUtils.getPullRequestsChangeMap(RequestType.ADD_LABEL);
        Iterator it1 = prLabelsMap.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry pr = (Map.Entry) it1.next();
            addPRLabel((String) pr.getKey(), (String) pr.getValue());
        }
    }

    /**
     * Add all the pull requests assignees.
     */
    public static void addPRComments() {
        LOGGER.info("============Add Comments============");
        Map<String, String> prCommentsMap = FileUtils.getPullRequestsChangeMap(RequestType.ADD_COMMENT);
        Iterator it = prCommentsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pr = (Map.Entry) it.next();
            addPRComment((String) pr.getKey(), (String) pr.getValue());
        }
    }

    /**
     * Assigns a PR to a particular user.
     *
     * @param prNo
     * @param assignee
     */
    private static void addPRAssignee(String prNo, String assignee) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = null;
            if (prNo != null && StringUtils.isNotBlank(assignee)) {
                HttpUriRequest post = restHandler.getHttpPostAssignees(prNo, assignee);
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new RuntimeException(
                            "Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                }
                LOGGER.info("PR#" + prNo + ":" + assignee);
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding assignee", e);
        }
    }

    /**
     * Removes assignee from a particular PR.
     *
     * @param prNo
     * @param assignee
     */
    private static void removePRAssignee(String prNo, String assignee) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = null;
            if (prNo != null && StringUtils.isNotBlank(assignee)) {
                HttpUriRequest post = restHandler.getHttpDeleteAssignees(prNo, assignee);
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new RuntimeException(
                            "Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                }
                LOGGER.info("PR#" + prNo + ":" + assignee);
            }
        } catch (Exception e) {
            LOGGER.error("Error while removing assignee", e);
        }
    }

    /**
     * Add labels to a particular PR.
     *
     * @param prNo
     * @param label
     */
    private static void addPRLabel(String prNo, String label) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = null;
            if (prNo != null && StringUtils.isNotBlank(label)) {
                HttpUriRequest post = restHandler.getHttpPostLabel(prNo, label);
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new RuntimeException(
                            "Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                }
                LOGGER.info("PR#" + prNo + ":" + label);
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding label", e);
        }
    }

    /**
     * Removes label from a particular PR.
     *
     * @param prNo
     * @param label
     */
    private static void removePRLabel(String prNo, String label) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = null;
            if (prNo != null && StringUtils.isNotBlank(label)) {
                HttpUriRequest post = restHandler.getHttpDeleteLabel(prNo, label);
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new RuntimeException(
                            "Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                }
                LOGGER.info("PR#" + prNo + ":" + label);
            }
        } catch (Exception e) {
            LOGGER.error("Error while removing label", e);
        }
    }

    /**
     * Add comments to a particular PR.
     *
     * @param prNo
     * @param comments
     */
    private static void addPRComment(String prNo, String comments) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = null;
            if (prNo != null) {
                comments = comments!=null ? comments : Constants.DEFAULT_COMMENT;
                HttpUriRequest post = restHandler.getHttpPostComment(prNo, comments);
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
                    throw new RuntimeException(
                            "Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                }
                LOGGER.info("PR#" + prNo + ":" + comments);
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding comment", e);
        }
    }
}
