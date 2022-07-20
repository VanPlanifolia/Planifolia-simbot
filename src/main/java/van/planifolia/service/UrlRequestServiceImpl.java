package van.planifolia.service;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import van.planifolia.action.UrlRequestAction;

/**
 * Url请求service的具体实现类,这个模块只负责调用action与dao来完成请求。
 * @author Van.Planifolia
 */
@Beans
public class UrlRequestServiceImpl implements UrlRequestService{
    //注入action的依赖
    @Depend
    UrlRequestAction urlRequestAction;

    @Override
    public void getRandomMusic(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.getRandomMusic(groupMsg, sender);
    }

    @Override
    public void repeat(GroupMsg groupMsg, Sender sender) {
        sender.sendGroupMsg(groupMsg.getGroupInfo(),groupMsg.getMsgContent());
    }

    @Override
    public void getRandomSt(GroupMsg groupMsg, Sender sender, Setter setter) {
        urlRequestAction.getRandomSt(groupMsg,sender,setter);
    }

    @Override
    public void getRandomImageECY(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.getRandomImageECY(groupMsg,sender);
    }

    @Override
    public void getRandomImageMen(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.getRandomImageMen(groupMsg, sender);
    }

    @Override
    public void getRandomHitokoto(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.getRandomHitokoto(groupMsg,sender);
    }

    @Override
    public void getTodayForHis(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.getTodayForHis(groupMsg, sender);
    }

    @Override
    public void getRandomImageTx(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.getRandomImageTx(groupMsg, sender);
    }

    @Override
    public void getDailyFortune(Sender sender, GroupMsg groupMsg) {
        urlRequestAction.getDailyFortune(groupMsg, sender);
    }

    @Override
    public void song(Sender sender, GroupMsg groupMsg) {
        urlRequestAction.song(groupMsg, sender);
    }

    @Override
    public void sendMusic(Sender sender, GroupMsg groupMsg) {
        urlRequestAction.sendMusic(groupMsg, sender);
    }

    @Override
    public void searchSourceForImage(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.searchSourceForImage(groupMsg, sender);
    }

    @Override
    public void serachCartoonForName(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.serachCartoonForName(groupMsg, sender);
    }

    @Override
    public void sendCartoon(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.sendCartoon(groupMsg,sender);
    }
}
