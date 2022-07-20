package van.planifolia.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.ListenGroup;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.results.GroupList;
import love.forte.simbot.api.message.results.SimpleGroupInfo;
import love.forte.simbot.api.sender.Getter;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.filter.MatchType;
import van.planifolia.service.GroupToolService;
import van.planifolia.service.ManagerService;
import van.planifolia.service.UrlRequestService;
import van.planifolia.util.Constant;

import javax.swing.filechooser.FileSystemView;
import java.util.Iterator;

/**
 * 群组监听的类，监听群组中的消息去调用对应的service相当于controller。
 * tips:需要注意的是simbot框架内置了一个简单的ioc容器，所有需要交给ioc容器管理的bean要使用@Beans注解标注，
 * 而获取这个bean则需要使用@Depend注解标注。
 * 而且我们要是想要使用group拦截器那么我们就需要在监听函数上添加注解 @ListenGroup("组名")
 * @author Van.Planifolia
 */

@ListenGroup("Group1")
@Beans
public class GroupListener {
    //注入service层
    @Depend
    public UrlRequestService urlRequestService;
    @Depend
    public ManagerService managerService;
    @Depend
    public GroupToolService groupToolService;
    /**
     * 复读的listener，做为示范
     * @Filter 消息过滤器注解，其中value作为方法触发关键字，matchType作为匹配形式，具体用法建议去看框架作者的api文档
     * URL:https://www.yuque.com/simpler-robot/simpler-robot-doc/wyt74o
     * @param groupMsg 消息容器
     * @param sender 送信器
     */
    @OnGroup
    @Filter(value = "复读",matchType = MatchType.CONTAINS)
    public void repeat(GroupMsg groupMsg, Sender sender){
        urlRequestService.repeat(groupMsg, sender);
    }

    /**
     * bot关机的listen
     * @param groupMsg
     * @param sender
     */
    @OnGroup
    @Filter(value = "关机",trim = true,atBot = true)
    public void offBot(GroupMsg groupMsg,Sender sender){
        managerService.offBot(sender, groupMsg);
    }

    /**
     * bot开机的listen仅仅是用来触发拦截器
     * @param groupMsg
     * @param sender
     */
    @OnGroup
    @Filter(value = "开机",trim = true,atBot = true)
    public void onBot(GroupMsg groupMsg, Sender sender, Getter getter){
        //获取本地的桌面路径保存到常量工具类中
        if (Constant.parentPath==null){ Constant.parentPath = FileSystemView.getFileSystemView()
                .getHomeDirectory().getAbsolutePath(); }

        //拿到群组信息的迭代器
        Iterator<SimpleGroupInfo> iterator = getter.getGroupList().getResults().iterator();
        //便利所有群组，加入到list中
        while (iterator.hasNext()){
            Constant.groupList.add(iterator.next().getGroupCode());
        }
        sender.sendGroupMsg(groupMsg.getGroupInfo(),"开机啦！");
    }
    /**
     * 请求随机音乐的listen
     */
    @OnGroup
    @Filter(atBot = true,value = "来首音乐",matchType = MatchType.CONTAINS)
    public void RandomMusic(GroupMsg groupMsg,Sender sender){
        urlRequestService.getRandomMusic(groupMsg, sender);
    }

    /**
     * 请求Setu的listen
     */
    @OnGroup
    @Filter(atBot = true,value = "来点色图",matchType = MatchType.CONTAINS)
    public void RandomSt(GroupMsg groupMsg, Sender sender, Setter setter){
        urlRequestService.getRandomSt(groupMsg, sender,setter);
    }
    /**
     * 请求随机二次元图片的listen
     */
    @OnGroup
    @Filter(atBot = true,value = "来点二次元",matchType = MatchType.CONTAINS)
    public void getRandomImageECY(GroupMsg groupMsg,Sender sender){
        urlRequestService.getRandomImageECY(groupMsg, sender);
    }

    /**
     * 请求随机Men图片Listen
     */
    @OnGroup
    @Filter(atBot = true,value = "老婆",matchType = MatchType.CONTAINS)
    @Filter(atBot = true,value = "Men酱",matchType = MatchType.CONTAINS)
    public void getImageMen(GroupMsg groupMsg, Sender sender){
        urlRequestService.getRandomImageMen(groupMsg,sender);
    }
    /**
     * 随机骚话api
     */
    @OnGroup
    @Filter(atBot = true)
    public void getRandomHitokoto(GroupMsg groupMsg, Sender sender) {
        urlRequestService.getRandomHitokoto(groupMsg, sender);
    }
    /**
     * 历史上的今天
     */
    @OnGroup
    @Filter(atBot = true,value = "历史上的今天",matchType = MatchType.CONTAINS)
    public void getTodayForHis(GroupMsg groupMsg,Sender sender) {
        urlRequestService.getTodayForHis(groupMsg, sender);
    }
    /**
     * at全体成员
     */
    @OnGroup
    @Filter(value = "/atAll", trim = true, matchType = MatchType.STARTS_WITH)
    @Filter(value = "/atall", trim = true, matchType = MatchType.STARTS_WITH)
    @Filter(value = "/at全体", trim = true, matchType = MatchType.STARTS_WITH)
    @Filter(value = "/艾特全体", trim = true, matchType = MatchType.STARTS_WITH)
    public void atAllPeople(GroupMsg groupMsg, Sender sender) {
        managerService.atAllPeople(sender, groupMsg);
    }

    /**
     * 随机头像
     */
    @OnGroup
    @Filter(atBot = true,value = "来张头像",matchType = MatchType.CONTAINS)
    public void getRandomImageTx(GroupMsg groupMsg, Sender sender){
        urlRequestService.getRandomImageTx(groupMsg, sender);
    }
    /**
     * 每日运势
     */
    @OnGroup
    @Filter(atBot = true,value = "今日运势",matchType = MatchType.CONTAINS)
    @Filter(atBot = true,value = "运势",matchType = MatchType.CONTAINS)
    @Filter(atBot = true,value = "占卜",matchType = MatchType.CONTAINS)
    public void getDailyFortune(GroupMsg groupMsg,Sender sender){
        urlRequestService.getDailyFortune(sender, groupMsg);
    }

    /**
     * 点歌
     */
    @OnGroup
    @Filter(value = "/点歌",matchType = MatchType.CONTAINS)
    public synchronized void  song(GroupMsg groupMsg,Sender sender){
        urlRequestService.song(sender, groupMsg);
    }

    /**
     * 发送
     */
    @OnGroup
    @Filter(atBot = true)
    public void sendMusic(GroupMsg groupMsg,Sender sender){
        urlRequestService.sendMusic(sender, groupMsg);
    }

    /**
     * 万能搜索
     */
    @OnGroup
    @Filter(value = "/搜索",matchType = MatchType.CONTAINS)
    public void searchSourceForImage(GroupMsg groupMsg,Sender sender){
        urlRequestService.searchSourceForImage(groupMsg, sender);
    }

    /**
     * 计时器
     */
    @OnGroup
    @Filter(value = "/Timer" ,matchType = MatchType.CONTAINS)
    @Filter(value = "/timer" ,matchType = MatchType.CONTAINS)
    public void requestForTimer(GroupMsg groupMsg, Sender sender){
        groupToolService.requestTimer(groupMsg, sender);
    }
    /**
     * 计时器列表
     */
    @OnGroup
    @Filter(value = "/ls")
    @Filter(value = "计时器队列")
    public void timerList(GroupMsg groupMsg,Sender sender){
        groupToolService.timerList(groupMsg, sender);
    }
    /**
     * 关闭一个计时器
     */
    @OnGroup
    @Filter(value = "/stop" ,matchType = MatchType.CONTAINS)
    @Filter(value = "/Stop" ,matchType = MatchType.CONTAINS)
    public void stopTimer(GroupMsg groupMsg, Sender sender){
        groupToolService.stopTimer(groupMsg, sender);
    }
    /**
     * 番剧搜索
     */
    @OnGroup
    @Filter(value = "/番剧搜索",matchType = MatchType.CONTAINS)
    public void serachCartoonForName(GroupMsg groupMsg,Sender sender){
        urlRequestService.serachCartoonForName(groupMsg, sender);
    }
    @OnGroup
    @Filter(atBot = true)
    public void sendCartoon(GroupMsg groupMsg,Sender sender){
        urlRequestService.sendCartoon(groupMsg, sender);
    }
}
