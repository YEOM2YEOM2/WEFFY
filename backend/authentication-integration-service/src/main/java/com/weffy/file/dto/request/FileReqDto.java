package com.weffy.file.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileReqDto {

    private String conferenceId;
    private LocalDateTime start;
    private LocalDateTime end;
}
