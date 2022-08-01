package van.planifolia.listener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import van.planifolia.util.Constant;


/**
 * 本类用于私聊管理群组信息
 * @author Planifolia.Van
 */
@Beans
public class MyPrivateListener {
    /**
     *
     * @param privateMsg 群组信息
     * @param sender 送信器<br>
     * 用于查看当前已管理的群组信息
     */
    @OnPrivate
    @Filter(value = "/群组列表", trim = true, matchType = MatchType.CONTAINS)
    @Filter(value = "/glist", trim = true, matchType = MatchType.CONTAINS)
    public void getGroupList(PrivateMsg privateMsg, Sender sender){
        StringBuilder builder=new StringBuilder();
        for (String s : Constant.groupList) {
            builder.append(s).append("\n");
        }
        sender.sendPrivateMsg(privateMsg.getAccountInfo(), builder.toString());
        builder.delete(0,builder.length());
    }

    /**
     * 移除某一个已添加的群组信息
     */
    @OnPrivate
    @Filter(value = "/rm",matchType = MatchType.CONTAINS)
    public void rmOneGroup(Sender sender,PrivateMsg privateMsg){
        //切割消息正文
        String[] messageSplit=privateMsg.getText().split(" ");
        //拿到第二位置的字符串，也就是需要被添加的群组信息
        int index=0;
        try{
            index= Integer.parseInt(messageSplit[1])-1;
        }catch (Exception e){
            sender.sendPrivateMsg(privateMsg.getAccountInfo(),"消息格式不对奥。");
        }
        Constant.groupList.remove(index);
        sender.sendPrivateMsg(privateMsg.getAccountInfo(),"第"+index+"个群组已经移除！");
    }
    @OnPrivate
    @Filter(value = "/test", trim = true, matchType = MatchType.STARTS_WITH)
    public void replyPrivateMsg1(PrivateMsg privateMsg, Sender sender){
        // 获取消息正文。
        MessageContent msgContent = privateMsg.getMsgContent();
        sender.sendPrivateMsg(privateMsg, msgContent);
        CatCodeUtil catCodeUtil = CatCodeUtil.getInstance();
        String cat3 = catCodeUtil.getStringTemplate().face(9);
        sender.sendPrivateMsg(privateMsg, "表情：" + cat3);
    }
}
