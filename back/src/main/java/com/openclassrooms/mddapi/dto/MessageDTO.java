package com.openclassrooms.mddapi.dto;

import lombok.*;

/**
 * Data Transfer Object for generic message responses.
 * 
 * This DTO is used to send simple text messages back to clients,
 * typically for success confirmations or informational responses.
 * 
 * @author Cécile UMECKER
 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String message;
}
