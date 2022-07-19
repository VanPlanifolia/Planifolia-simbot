package van.planifolia.service;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * 群组
 */
public interface GroupToolService {
    void requestTimer(GroupMsg groupMsg, Sender sender);
    void timerList(GroupMsg groupMsg,Sender sender);
    void stopTimer(GroupMsg groupMsg,Sender sender);
}
