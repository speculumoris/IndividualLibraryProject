package com.lib.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PublisherDTO {
    private String name;

    private boolean builtIn =false;


}
