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

   // private static Map<String, Boolean> sessionRecordings;

    //private static Map<String, String> mapIdentificationTokens;

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

//    public static Map<String, Boolean> getSessionRecordings(){
//        if(sessionRecordings == null){
//            sessionRecordings = new ConcurrentHashMap<>();
//        }
//        return sessionRecordings;
//    }

//    public static Map<String, String> getMapIdentificationTokens(){
//        if(mapIdentificationTokens == null){
//            mapIdentificationTokens = new ConcurrentHashMap<>();
//        }
//        return mapIdentificationTokens;
//    }



}
