package van.planifolia.task;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.timer.Cron;
import love.forte.simbot.timer.EnableTimeTask;
import van.planifolia.util.ApiEnum;
import van.planifolia.util.Constant;
import van.planifolia.util.SourceDownloader;

import java.io.File;

/**
 * 这个类仅仅是用作每日新闻
 * @author Van.Planifolia
 */
@Beans
@EnableTimeTask
public class DailyTask {
    //注入依赖
    @Depend
    private MessageContentBuilderFactory messageContentBuilderFactory;
    CatCodeUtil catCodeUtil = CatCodeUtil.getInstance();//使用猫猫码
    //使用cron表达式来完成每日的新闻
    @Cron("0 0 8 * * ?")
//    @Cron("1 * * * * ? ")
    public void dailyNewsTask(){
        try {
            String dwImage = SourceDownloader.downloadImg(ApiEnum.DailyNews.getValue());
            File file = new File(dwImage);
            String image = catCodeUtil.toCat("image", true, "file=" + file.getAbsolutePath());
            for (String s : Constant.groupList) {
                Constant.sender.SENDER.sendGroupMsg(s,"早间新闻");
                Constant.sender.SENDER.sendGroupMsg(s, image);
            }
            file.delete();
        }catch (Exception e){
            Constant.sender.SENDER.sendGroupMsg("","上游API错误!");
        }
    }
}
