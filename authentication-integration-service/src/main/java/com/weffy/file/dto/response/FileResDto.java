package com.weffy.file.dto.response;

import com.weffy.file.entity.Files;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResDto {

    private Long id;
    private String url;
    private String title;
    private Long size;
    private String conferenceId;


    public FileResDto of(Files files) {
        FileResDto fileResDto = new FileResDto();
        fileResDto.setUrl(files.getUrl());
        fileResDto.setTitle(files.getTitle());
        fileResDto.setSize(files.getSize());
        fileResDto.setConferenceId(files.getConferenceId());
        return fileResDto;
    }
}
