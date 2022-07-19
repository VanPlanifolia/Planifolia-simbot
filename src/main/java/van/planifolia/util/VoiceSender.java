package van.planifolia.util;

import catcode.CatCodeUtil;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import java.io.File;

/**
 * 发送语音的工具类，只有一个方法用于根据音频文件发送语音消息
 * @author Van.Planifolia
 */
public class VoiceSender {
    /**
     * 发送语音的方法，下面是他的重载
     * @param nameValue 文件名
     * @param resourcesValue 资源url
     * @param artValue 歌手名
     */
    static CatCodeUtil catCodeUtil = CatCodeUtil.getInstance();//使用猫猫码
    public static void getMusicSources(GroupMsg groupMsg, Sender sender,String nameValue,String resourcesValue,String artValue){
        if(!nameValue.equals("")){
            String dwMusic="";
            //下载音乐，
            try {
                dwMusic=SourceDownloader.dowLoadMusic(resourcesValue,nameValue,artValue);
            }catch (Exception e){
                sender.sendGroupMsg(groupMsg.getGroupInfo(),"网络波动下载音乐失败！");
            }
            //关联文件
            File file=new File(dwMusic);
            //猫猫码构建音乐消息
            String music=catCodeUtil.toCat("voice",false,"file="+file.getAbsolutePath());
            try {
                sender.sendGroupMsg(groupMsg.getGroupInfo(),music);

            }catch (Exception e){
//                sender.sendGroupMsg(groupMsg.getGroupInfo(),"因为某些原因语音发送失败！");
            }finally {
                file.delete();
            }
        }
    }
    public static void getMusicSources(GroupMsg groupMsg, Sender sender,String sonResourcesValue){
        if(!"".equals(sonResourcesValue)){
            String dwMusic="";
            //下载音乐，
            try {
                dwMusic=SourceDownloader.dowLoadMusic(sonResourcesValue,"temp","");
            }catch (Exception e){
                sender.sendGroupMsg(groupMsg.getGroupInfo(),"网络波动下载音乐失败,可能是资源丢失或者没有版权！");
            }
            //关联文件
            File file=new File(dwMusic);
            //猫猫码构建音乐消息
            String music=catCodeUtil.toCat("voice",false,"file="+file.getAbsolutePath());
            try {
                sender.sendGroupMsg(groupMsg.getGroupInfo(),music);
            }catch (Exception e){
                sender.sendGroupMsg(groupMsg.getGroupInfo(),"因为某些原因语音发送失败！");
            }finally {
                file.delete();
            }
        }else {
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"很遗憾没找到对应的歌曲信息捏\uD83D\uDE1C");
        }
    }
}
