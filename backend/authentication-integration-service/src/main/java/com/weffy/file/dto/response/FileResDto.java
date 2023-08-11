package com.weffy.file.dto.response;

import com.weffy.file.entity.Files;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResDto {

    private String url;
    private String title;
    private String objectKey;
    private String conferenceId;


    public FileResDto of(Files files) {
        FileResDto fileResDto = new FileResDto();
        fileResDto.setUrl(files.getUrl());
        fileResDto.setTitle(files.getTitle());
        fileResDto.setObjectKey(files.getObjectKey());
        fileResDto.setConferenceId(files.getConferenceId());
        return fileResDto;
    }
}
