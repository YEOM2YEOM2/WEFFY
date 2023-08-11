package com.weffy.file.entity;


import com.weffy.common.entity.TimeEntity;
import com.weffy.file.dto.response.GetFileDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Files extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String title;
    private String objectKey;
    private String conferenceId;

    @Builder
    public Files(String url, String title, String objectKey, String conferenceId) {
        this.url = url;
        this.title = title;
        this.objectKey = objectKey;
        this.conferenceId = conferenceId;
    }

    public GetFileDto of() {
        GetFileDto getFileListDto = new GetFileDto();
        getFileListDto.setId(this.id);
        getFileListDto.setUrl(this.url);
        getFileListDto.setTitle(this.title);
        getFileListDto.setObjectKey(this.objectKey);
        return getFileListDto;
    }
}
