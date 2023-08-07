package io.openvidu.basic.java.ov;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Setter
public class ServerDB {

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private static OpenVidu openvidu; // 오픈비듀 서버 생성

    //private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

    //private Map<String, Boolean> sessionRecordings = new ConcurrentHashMap<>();

    private ServerDB(){
        openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
        System.out.println("come!!!!!!!!!!!!!!");
    }

    public static synchronized OpenVidu getInstance(){
        if(openvidu == null){
            new ServerDB();
        }
        return openvidu;
    }



}
