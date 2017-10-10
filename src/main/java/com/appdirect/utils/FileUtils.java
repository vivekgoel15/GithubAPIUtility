package com.appdirect.utils;

import com.appdirect.configuration.Constants;
import com.appdirect.enums.RequestType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vigoel on 13/6/16.
 */
public class FileUtils {

    private static Map<RequestType, Map<String, String>> pullRequestsMap = new HashMap();

    /**
     * Reads parameter from request type passed from file in job
     * @param requestType
     * @return
     */
    public static Map<String, String> readPRMapFromFile(RequestType requestType) throws IOException {
        Map<String, String> prRequestMap = new HashMap();
        BufferedReader bufferedReader = null;
        final String DELIMITER = ",";
        String line = "";
        bufferedReader = new BufferedReader(new FileReader(new File(Constants.UPLOAD_FILE_NAME)));
        // Read the header line
        bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null)
        {
            String[] pr = line.split(DELIMITER);
            if (pr.length>1) {
                prRequestMap.put(pr[Constants.PR_INDEX],
                        (requestType.getColumnIndex() == 5 && pr.length < 6) ? null : pr[requestType.getColumnIndex()]);
            }
        }
        bufferedReader.close();
        return prRequestMap;
    }

    /**
     * Loads the pull Request changes file into Maps for all request types.
     */
    public static void init() throws IOException {
        for (RequestType requestType : RequestType.values()) {
            pullRequestsMap.put(requestType, readPRMapFromFile(requestType));
        }
    }

    public static Map<String, String> getPullRequestsChangeMap(RequestType requestType) {
        return pullRequestsMap.get(requestType);
    }

}
