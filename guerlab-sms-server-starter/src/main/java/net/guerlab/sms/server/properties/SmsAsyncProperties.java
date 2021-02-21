package net.guerlab.sms.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * 短信异步配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.async")
public class SmsAsyncProperties {

    /**
     * 是否启用异步支持
     */
    private boolean enable = true;

    /**
     * 核心线程数量
     * <p>
     * 默认:
     * 可用处理器数量 (Runtime.getRuntime().availableProcessors())
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程数量
     * <p>
     * 默认:
     * 可用处理器数量 * 2 (Runtime.getRuntime().availableProcessors() * 2)
     */
    private int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 线程最大空闲时间
     * <p>
     * 默认:
     * 60
     */
    private long keepAliveTime = 60L;

    /**
     * 线程最大空闲时间单位
     * <p>
     * 默认:
     * TimeUnit.SECONDS
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 队列容量
     * <p>
     * 默认:
     * Integer.MAX_VALUE
     */
    private int queueCapacity = Integer.MAX_VALUE;

    /**
     * 拒绝策略
     * <p>
     * 可选值:
     * Abort、Caller、Discard、DiscardOldest
     * 默认:
     * Abort
     */
    private RejectPolicy rejectPolicy = RejectPolicy.Abort;
}
