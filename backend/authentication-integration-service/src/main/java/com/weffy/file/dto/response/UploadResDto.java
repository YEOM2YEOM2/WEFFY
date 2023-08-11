package com.weffy.file.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadResDto {
    String sessionToken;
    String classId;
    List<GetFileDto> files;

    public UploadResDto of(String sessionToken, String classId, List<GetFileDto> files) {
        UploadResDto res = new UploadResDto();
        res.sessionToken = sessionToken;
        res.classId = classId;
        res.files = files;
        return res;
    }
}
