package cn.chenmixuexi.bean;

public class OfflineMsg {
    private Integer id;

    private Integer userId;

    private String toName;

    private String fromName;

    private Integer msgType;

    private String msg;

    private Integer state;

    public OfflineMsg() {
    }

    public OfflineMsg(Integer id, Integer userId, String toName, String fromName, Integer msgType, String msg, Integer state) {
        this.id = id;
        this.userId = userId;
        this.toName = toName;
        this.fromName = fromName;
        this.msgType = msgType;
        this.msg = msg;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName == null ? null : toName.trim();
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName == null ? null : fromName.trim();
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}