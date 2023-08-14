package openvidu.meeting.service.java.history.entity;

import lombok.Getter;

@Getter
public enum Active {
    CREATE("CREATE"),
    CONNECTION("CONNECTION"),
    EXIT("EXIT"),
    LEAVE("LEAVE"),
    DELETE("DELETE"),
    REC_START("REC_START"),
    REC_END("REC_END");
    private String active;

    Active(String active){
        this.active = active;
    }

}
