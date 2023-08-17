package openvidu.meeting.service.java;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduRole;
import lombok.Getter;
import lombok.Setter;
import openvidu.meeting.service.java.conference.entity.UserRole;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class OpenviduDB {

    private static OpenVidu openvidu;

    private static String OPENVIDU_URL = "http://localhost:4443/";

    private static String OPENVIDU_SECRET = "MY_SECRET";

    private static Map<String, Map<String, UserRole>> mapSessionNamesTokens;

    private static Map<String, String> hostToken;

    private static Map<String, Integer> sessionRecordingNumber;

    private static Map<String, String> hostConnectionId;

    private static Map<String, Map<String, String>> sessionConnectionList;

    public static OpenVidu getOpenvidu(){
        if(openvidu == null){
            openvidu = new OpenVidu(OPENVIDU_URL,OPENVIDU_SECRET);
        }
        return openvidu;
    }

    public static Map<String, Map<String, UserRole>> getMapSessionNameTokens(){
        if(mapSessionNamesTokens == null){
            mapSessionNamesTokens = new ConcurrentHashMap<>();
        }
        return mapSessionNamesTokens;
    }

    public static Map<String, String> getHostToken(){
        if(hostToken == null){
            hostToken = new ConcurrentHashMap<>();
        }
        return hostToken;
    }

    public static Map<String, String> getHostConnectionId(){
        if(hostConnectionId == null){
            hostConnectionId = new ConcurrentHashMap<>();
        }
        return hostConnectionId;
    }

    public static Map<String, Map<String, String>> getSessionConnectionList(){
        if(sessionConnectionList == null){
            sessionConnectionList = new ConcurrentHashMap<>();
        }
        return sessionConnectionList;
    }



}
