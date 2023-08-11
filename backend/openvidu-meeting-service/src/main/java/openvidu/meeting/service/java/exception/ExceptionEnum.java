package openvidu.meeting.service.java.exception;


import org.springframework.http.HttpStatus;

/*
- 400 이미 존재하는 방입니다.
- 401 존재하지 않는 방입니다.
- 402 참가하지 않은 방입니다. => 사용자가 참가한 적이 없는데 방을 나가려는 경우

- 403 녹화를 진행하고 있습니다. => 녹화를 진행하고 있는데 다른 사람이 또 녹화를 진행하려는 경우
- 404 녹화를 할 수 있는 권한이 없습니다.
- 405 녹화를 생성하지 못했습니다.
- 406 녹화를 멈출 수 없습니다. 녹화를 시작하고 있는지 다시 확인하세요.
- 407 녹화를 삭제할 수 없습니다. 녹화 목록을 다시 확인하세요.
- 408 녹화를 확인할 수 없습니다.

- 409 : 404 ERROR
*/
public enum ExceptionEnum {

    // CONFERENCE
    CONFERENCE_EXIST(HttpStatus.BAD_REQUEST,4000,"이미 존재하는 방입니다."),
    CONFERENCE_NOT_EXIST(HttpStatus.BAD_REQUEST,4001,"존재하지 않는 방입니다."),
    CONFERENCE_NOT_PARTICIPATED(HttpStatus.BAD_REQUEST,4002,"참가하지 않은 방입니다."),


    // RECORDING
    RECORDING_CONTINUE(HttpStatus.BAD_REQUEST,4003,"녹화를 진행하고 있습니다."),
    RECORDING_NOT_ALLOWED(HttpStatus.BAD_REQUEST,4004,"녹화를 할 수 있는 권한이 없습니다."),
    RECORDING_GENERATION_ERROR(HttpStatus.BAD_REQUEST,4005,"녹화를 생성하지 못했습니다."),
    RECORDING_STOP_FAILED(HttpStatus.BAD_REQUEST,4006,"녹화를 멈출 수 없습니다. 녹화를 시작하고 있는지 다시 확인하세요."),
    RECORDING_DELETE_FAILED(HttpStatus.BAD_REQUEST,4007,"녹화를 삭제할 수 없습니다. 녹화 목록을 다시 확인하세요."),
    RECORDING_VALIDATION_FAILED(HttpStatus.BAD_REQUEST,4008,"녹화를 확인할 수 없습니다."),
    GENERIC_ERROR(HttpStatus.BAD_REQUEST,4009,"404 ERROR"),;


    private HttpStatus status;
    private int code;
    private String description;

    private ExceptionEnum(HttpStatus status, int code, String description){
        this.code=code;
        this.status=status;
        this.description=description;
    }


}
