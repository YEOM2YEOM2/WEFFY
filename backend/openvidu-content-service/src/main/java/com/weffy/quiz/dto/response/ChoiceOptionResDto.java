package com.weffy.quiz.dto.response;

import com.weffy.quiz.entity.ChoiceOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChoiceOptionResDto {

    private Long id;
    private String content;

    public static ChoiceOptionResDto of(ChoiceOption option) {
        ChoiceOptionResDto dto = new ChoiceOptionResDto();
        dto.id = option.getId();
        dto.content = option.getContent();
        return dto;
    }
}
