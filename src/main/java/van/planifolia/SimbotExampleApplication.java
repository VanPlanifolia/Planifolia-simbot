package van.planifolia;
import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;
import van.planifolia.util.Constant;

/**
 * Planifolia-simbot 的启动类，使用了ForteScarlet提供的simbot框架特此感谢。
 * <p>
 * 此处的注解配置了两个配置文件：
 * <ul>
 *     <li>../simbot.yml</li>
 *     上面的文件是作为正式部署的bot账号密码文件，需要注意的是必须要在jar包的同级下也就是这个文件要与jar包平行
 *     <li>simbot-dev.yml</li>
 *     这个文件是最为测试环境的bot账号密码文件。
 * </ul>
 * 其中，{@code simbot-dev.yml} 是一个测试环境的配置文件，只有当启动参数中存在 {@code --Sdev} 的时候才会被使用。
 * 如果你不需要一些特殊的配置文件，那么可以直接使用 {@code @SimbotApplication}.
 * <p>
 * 默认情况下，默认的配置文件名称为 {@code simbot.yml} 或 {@code simbot.properties}
 *
 * @author ForteScarlet，Van.Planifolia
 */

@SimbotApplication({
        @SimbotResource(value = "../simbot.yml", orIgnore = true),
        @SimbotResource(value = "simbot-dev.yml", orIgnore = true, command = "dev"),
})
public class SimbotExampleApplication {

    public static void main(String[] args) {
        /*
            run方法的第一个参数是一个标注了@SimbotApplication注解的启动类。
            第二个参数是main方法得到的启动参数。
         */
        SimbotContext simbotContext = SimbotApp.run(SimbotExampleApplication.class, args);
        //获取到bot管理器保存到常量类中
        Constant.botManager = simbotContext.getBotManager();
        //获取到送信器保存到常量工具类中
        Constant.sender=simbotContext.getBotManager().getDefaultBot().getSender();
        System.out.println(Constant.sender);
    }
}
