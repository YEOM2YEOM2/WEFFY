package com.weffy.file.dto.response;

import com.weffy.file.entity.Files;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFileDto {

    private Long id;
    private String url;
    private String title;
    private Long size;

}
