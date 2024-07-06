package com.argus.deal.dto;


import com.argus.deal.model.enums.Theme;

import java.util.UUID;

/**
 * EmailMessage
 *
 * @author Maxim Chistyakov
 */
public class EmailMessage {

    private String address;

    private Theme theme;

    private UUID statementID;

}
