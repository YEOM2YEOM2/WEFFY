package com.weffy.mattermost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weffy.user.entity.WeffyUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
