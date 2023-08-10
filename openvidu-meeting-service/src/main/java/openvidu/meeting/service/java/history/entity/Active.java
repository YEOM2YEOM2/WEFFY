package openvidu.meeting.service.java.history.entity;

import lombok.Getter;

@Getter
public enum Active {
    CREATE("CREATE"),
    CONNECTION("CONNECTION"),
    EXIT("EXIT"),
    LEAVE("LEAVE"),
    DELETE("DELETE");
    private String active;

    Active(String active){
        this.active = active;
    }

}
