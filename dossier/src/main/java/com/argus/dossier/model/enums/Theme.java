package com.argus.dossier.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Theme
 * </p>
 * Mail theme
 *
 * @author Maxim Chistyakov
 */
@RequiredArgsConstructor
@Getter
public enum Theme {
    FINISH_REGISTRATION("finish-registration"),
    CREATE_DOCUMENTS("create-documents"),
    SEND_DOCUMENTS("send-documents"),
    SEND_SES("send-ses"),
    CREDIT_ISSUED("credit-issued"),
    STATEMENT_DENIED("statement-denied");

    private final String topic;

}
