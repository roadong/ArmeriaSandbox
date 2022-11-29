package com.gl.springsandbox.api.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SignUpInfo {
    private String userName;
    private String password;
    private String email;
    private String nickName;

    private Map<String, Object> extraInfo;

    @JsonProperty
    public void initExtraInfo(Map<String, Object> extraInfo) {

    }
}
