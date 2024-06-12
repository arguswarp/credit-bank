package com.argus.deal.dto;

import com.argus.deal.model.enums.ChangeType;
import com.argus.deal.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * StatementStatusHistoryDto.
 *
 * @author Maxim Chistyakov
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementStatusHistoryDto {

    private Status status;

    private LocalDateTime time;

    private ChangeType changeType;

}
