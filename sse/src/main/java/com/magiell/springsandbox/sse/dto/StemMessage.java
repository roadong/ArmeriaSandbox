package com.magiell.springsandbox.sse.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class StemMessage {
    private String idType;

    // unicast client target
    private String clientId;
    private Object message;
}
