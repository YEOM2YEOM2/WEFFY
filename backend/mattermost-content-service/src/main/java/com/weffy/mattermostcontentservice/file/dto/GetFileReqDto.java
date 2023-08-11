package com.weffy.mattermostcontentservice.file.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetFileReqDto {
    private String sessionToken;
    private String classId;
    private List<FileDto> files;
}
