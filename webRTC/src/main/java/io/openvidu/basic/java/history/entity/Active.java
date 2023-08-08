package io.openvidu.basic.java.history.entity;

import lombok.Getter;

@Getter
public enum Active {
    JOIN("JOIN"),
    EXIT("EXIT"),
    CREATE("CREATE");
    private String active;

    Active(String active){
        this.active = active;
    }

}
