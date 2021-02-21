package net.guerlab.sms.server.properties;

/**
 * 拒绝策略
 *
 * @author guer
 */
public enum RejectPolicy {
    /**
     *
     */
    Abort,
    /**
     *
     */
    Caller,
    /**
     *
     */
    Discard,
    /**
     *
     */
    DiscardOldest
}
