package cn;

import cn.chenmixuexi.util.JsonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class 是 {
    public static void main(String[] args) {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        System.out.println(objectNode.get("name"));
    }
}
