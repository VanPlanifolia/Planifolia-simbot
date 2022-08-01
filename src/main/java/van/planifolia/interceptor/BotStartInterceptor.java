package van.planifolia.interceptor;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.core.intercept.FixedRangeGroupedListenerInterceptor;
import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.ListenerInterceptContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import van.planifolia.util.Constant;

import java.util.Objects;

/**
 * 群组消息的过滤器,主要是用来实现bot的开关机
 * @author Van.Planifolia
 */
@Beans
public class BotStartInterceptor extends FixedRangeGroupedListenerInterceptor {
    /**
     * 监听器的组别信息
     */
    @NotNull
    @Override
    protected String[] getGroupRange() {
        return new String[]{"Group1"};
    }
    /**
     * 拦截处理方法，用来拦截关机时的所有请求
     */
    @NotNull
    @Override
    protected InterceptionType doIntercept(@NotNull ListenerInterceptContext context, @Nullable String group) {
        //如果收到消息为开机
        if("开机".equals(Objects.requireNonNull(context.getMsgGet().getText()).trim())){
            //如果收到开机的消息则将BotState置为true，然后放行
            Constant.BotState=true;
            return InterceptionType.PASS;
        }
        //如果状态为ture则放行，否则就拦截
       if(Constant.BotState){
           System.out.println("放行了");
           return InterceptionType.PASS;
       }else {
           System.out.println("拦截了");
           return InterceptionType.HEAD_OFF;
       }
    }
}
