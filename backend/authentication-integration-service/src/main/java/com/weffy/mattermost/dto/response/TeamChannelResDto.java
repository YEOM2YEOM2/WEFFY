package com.weffy.mattermost.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamChannelResDto {
    private Long id;
    private String identification;
    private String name;
    private List<ChannelDto> channels;

    @Getter
    @Setter
    public static class ChannelDto {
        private Long id;
        private String identification;
        private String name;
        private boolean isAdmin;
    }
}
