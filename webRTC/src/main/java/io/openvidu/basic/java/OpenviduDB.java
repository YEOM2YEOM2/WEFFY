package io.openvidu.basic.java;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class OpenviduDB {

    private static OpenVidu openvidu;

    private static String OPENVIDU_URL = "http://localhost:4443/";

    private static String OPENVIDU_SECRET = "MY_SECRET";

    private static Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens;

    private static Map<String, Boolean> sessionRecordings;

    public static OpenVidu getOpenvidu(){
        if(openvidu == null){
            openvidu = new OpenVidu(OPENVIDU_URL,OPENVIDU_SECRET);
        }
        return openvidu;
    }

    public static Map<String, Map<String, OpenViduRole>> getMapSessionNameTokens(){
        if(mapSessionNamesTokens == null){
            mapSessionNamesTokens = new ConcurrentHashMap<>();
        }
        return mapSessionNamesTokens;
    }

    public static Map<String, Boolean> getSessionRecordings(){
        if(sessionRecordings == null){
            sessionRecordings = new ConcurrentHashMap<>();
        }
        return sessionRecordings;
    }



}
