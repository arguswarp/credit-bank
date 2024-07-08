package com.argus.dossier.dto;



import com.argus.dossier.model.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * EmailMessage
 *
 * @author Maxim Chistyakov
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {

    private String address;

    private Theme theme;

    private UUID statementID;

}
