package cn.chenmixuexi.service;

import cn.chenmixuexi.bean.OfflineMsg;
import cn.chenmixuexi.bean.User;

import java.util.List;
import java.util.Set;

public interface OfflineService {

    List<OfflineMsg> getOfflineMsg(String name);

    boolean updateOfflineMsg(OfflineMsg next);
}
