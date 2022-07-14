package van.planifolia.util;

import java.util.Date;
import java.util.Objects;
import java.util.Timer;

/**
 * 继承了Timer类并且对他进行了进一步的封装，增加了一些常用的成员属性与gs方法，并且重写了equals方法与hashcode方法
 */
public class TimerPlus extends Timer {
    //计时器任务name
    //计时器开始时间
    String name;
    Date startTime;
    //构造器
    public TimerPlus(Date startTime){ this.startTime=startTime; }

    public TimerPlus(){}

    //gs方法

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimerPlus timerPlus = (TimerPlus) o;
        return Objects.equals(name, timerPlus.name) && Objects.equals(startTime, timerPlus.startTime);
    }

    @Override
    public int hashCode() {
        return Math.abs(startTime.hashCode());
    }
}
