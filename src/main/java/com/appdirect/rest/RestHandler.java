package com.appdirect.rest;

import com.appdirect.configuration.Constants;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by vigoel on 13/6/16.
 */
public class RestHandler {

    private static final Logger LOGGER = Logger.getLogger(RestHandler.class);

    private static String username;
    private static String accessToken;

    public RestHandler(String username, String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }

    /**
     * This methods creates the Post request object for updating assignee with
     * parameters and headers.
     *
     * @param prNo
     *            pullRequest#
     * @param assignees
     *            new assignee for the pull request
     * @return HttpPost object with request parameters and headers
     * @throws UnsupportedEncodingException
     */
    public HttpUriRequest getHttpPostAssignees(String prNo, String assignees) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(Constants.GITHUB_BASE_URL + "/repos/" + Constants.REPOSITORY + "/issues/" + prNo);
        setHeaders(post);
        try {
            String[] assigneesArray = assignees.split(Constants.LABELS_ASSIGNEES_SEPARATOR);
            JSONObject json = new JSONObject();
            if (assigneesArray.length == 1) {
                try {
                    json.put("assignee", assigneesArray[0]);
                } catch (JSONException e) {
                    LOGGER.error("Error creating Post request", e);
                }
            } else {
                JSONArray jsonArray = new JSONArray(Arrays.asList(assigneesArray));
                json.put("assignees", jsonArray);
            }
            post.setEntity(new StringEntity(json.toString()));
        } catch (Exception e) {
            LOGGER.error("Error while adding label", e);
        }
        return post;
    }

    /**
     * This methods creates the Post request object for updating assignee with
     * parameters and headers.
     *
     * @param prNo
     *            pullRequest#
     * @param assignees
     *            new assignee for the pull request
     * @return HttpPost object with request parameters and headers
     * @throws UnsupportedEncodingException
     */
    public HttpUriRequest getHttpDeleteAssignees(String prNo, String assignees) throws UnsupportedEncodingException {
        HttpDeleteWithBody post = new HttpDeleteWithBody(Constants.GITHUB_BASE_URL + "/repos/" + Constants.REPOSITORY + "/issues/" + prNo + "/assignees");
        setHeaders(post);
        try {
            String[] assigneesArray = assignees.split(Constants.LABELS_ASSIGNEES_SEPARATOR);
            JSONObject json = new JSONObject();
            JSONArray jsonArray = new JSONArray(Arrays.asList(assigneesArray));
            json.put("assignees", jsonArray);
            post.setEntity(new StringEntity(json.toString()));
        } catch (Exception e) {
            LOGGER.error("Error while removing assignees", e);
        }
        return post;
    }

    /**
     * This methods creates the Post request object for adding label with
     * parameters and headers.
     *
     * @param prNo
     *            pullRequest#
     * @param labels
     *            new labels for the pull request
     * @return HttpPost object with request parameters and headers
     * @throws UnsupportedEncodingException
     */
    public HttpUriRequest getHttpPostLabel(String prNo, String labels) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(Constants.GITHUB_BASE_URL + "/repos/" + Constants.REPOSITORY + "/issues/" + prNo + "/labels");
        setHeaders(post);
        JSONArray json = new JSONArray(Arrays.asList(labels.split(Constants.LABELS_ASSIGNEES_SEPARATOR)));
        post.setEntity(new StringEntity(json.toString()));
        return post;
    }

    /**
     * This methods creates the Delete request object for removing label with
     * parameters and headers.
     *
     * @param prNo
     *            pullRequest#
     * @param label
     *            label to remove from the pull request
     * @return HttpPost object with request parameters and headers
     * @throws UnsupportedEncodingException
     */
    public HttpUriRequest getHttpDeleteLabel(String prNo, String label) throws UnsupportedEncodingException {
        HttpDelete post = new HttpDelete(Constants.GITHUB_BASE_URL + "/repos/" + Constants.REPOSITORY + "/issues/" + prNo + "/labels/" + label);
        setHeaders(post);
        return post;
    }

    /**
     * This methods creates the Post request object for adding comment with
     * parameters and headers.
     *
     * @param prNo
     *            pullRequest#
     * @param comment
     *            new comment for the pull request
     * @return HttpPost object with request parameters and headers
     * @throws UnsupportedEncodingException
     */
    public HttpUriRequest getHttpPostComment(String prNo, String comment) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(Constants.GITHUB_BASE_URL + "/repos/" + Constants.REPOSITORY + "/issues/" + prNo + "/comments");
        setHeaders(post);
        JSONObject json = new JSONObject();
        try {
            json.put("body", comment);
        } catch (JSONException e) {
            LOGGER.error("Error creating Post request", e);
        }
        post.setEntity(new StringEntity(json.toString()));
        return post;
    }

    /**
     * Sets the header parameters to request object.
     *
     * @param request
     */
    private static void setHeaders(HttpUriRequest request) {
        request.addHeader(HttpHeaders.ACCEPT, "application/vnd.github.cerberus-preview+json");
        request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.addHeader(HttpHeaders.AUTHORIZATION, getAuthKey(username, accessToken));
    }

    /**
     * Gets encrypted basic auth key to be used for Git API access
     *
     * @return
     */
    private static String getAuthKey(String username, String accessToken) {
        String auth = username + ":" + accessToken;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
        return "Basic " + new String(encodedAuth);
    }

    @NotThreadSafe
    class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";
        public String getMethod() { return METHOD_NAME; }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }
        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }
        public HttpDeleteWithBody() { super(); }
    }

}
