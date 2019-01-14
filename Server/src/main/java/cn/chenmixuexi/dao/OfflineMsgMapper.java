package cn.chenmixuexi.dao;

import cn.chenmixuexi.bean.OfflineMsg;

import java.util.List;

public interface OfflineMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfflineMsg record);

    int insertSelective(OfflineMsg record);

    OfflineMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfflineMsg record);

    int updateByPrimaryKey(OfflineMsg record);

    List<OfflineMsg> selectOfToname(String name);
}