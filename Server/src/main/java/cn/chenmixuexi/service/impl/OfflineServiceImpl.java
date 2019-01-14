package cn.chenmixuexi.service.impl;

import cn.chenmixuexi.bean.OfflineMsg;
import cn.chenmixuexi.dao.OfflineMsgMapper;
import cn.chenmixuexi.service.OfflineService;
import cn.chenmixuexi.util.MyBatisUtils;

import java.util.List;

public class OfflineServiceImpl implements OfflineService {
    private static OfflineMsgMapper offlineMsgMapper = MyBatisUtils.getSession().getMapper(OfflineMsgMapper.class);

    @Override
    public List<OfflineMsg> getOfflineMsg(String name) {
        return offlineMsgMapper.selectOfToname(name);
    }

    @Override
    public boolean updateOfflineMsg(OfflineMsg next) {
        return offlineMsgMapper.updateByPrimaryKeySelective(next)>0;
    }
}
