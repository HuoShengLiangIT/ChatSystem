package cn.chenmixuexi.contant;

public enum MsgType {
    MSG_LOGIN, //用户登录消息
    MSG_REGISTER, //用户注册消息
    MSG_FORGET_PWD, //用户忘记密码消息
    MSG_MODIFY_PWD, //修改密码休息
    MSG_CHAT, //一对一聊天消息
    MSG_CHAT_ALL, //群聊消息
    MSG_NOTIFY_ONLINE, //群发用户上线消息
    MSG_NOTIFY_OFFLINE, //群发用户下线消息
    MSG_OFFLINE, //用户下线消息
    MSG_GET_ALL_USERS, //获取所有在线用户信息
    MSG_TRANSFER_FILE, //传输文件消息
    MSG_CHECK_USER_EXIST, //用户是否存在【新增】
    MSG_OFFLINE_MSG_EXIST, //是否存在离线消息【新增】
    MSG_OFFLINE_FILE_EXIST, //是否存在离线文件【新增】
    MSG_ACK //响应消息
}
