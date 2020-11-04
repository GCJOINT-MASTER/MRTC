package cn.sh.qianyi.coinspider.logs;

import org.apache.log4j.*;

public class AssignedLevelAppender extends RollingFileAppender
{
    @Override
    public boolean isAsSevereAsThreshold(final Priority priority) {
        return this.getThreshold().equals(priority);
    }
}
