package com.appdirect.enums;

import com.appdirect.configuration.Constants;

/**
 * Created by vigoel on 13/6/16.
 */
public enum RequestType {
    REMOVE_ASSIGNEE(Constants.REMOVE_ASSIGNEE_INDEX),
    ADD_ASSIGNEE(Constants.ADD_ASSIGNEE_INDEX),
    REMOVE_LABEL(Constants.REMOVE_LABEL_INDEX),
    ADD_LABEL(Constants.ADD_LABEL_INDEX),
    ADD_COMMENT(Constants.ADD_COMMENT_INDEX);

    private int columnIndex;

    private RequestType(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }
}
