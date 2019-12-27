package com.github.x4096.common.utils.data.redis.enums;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-07-20 11:10
 * @Description: Redis 搭建模式
 */
public enum RedisStrategyEnum {

    /**
     * 单节点模式
     */
    SINGLE,

    /**
     * 单节点 连接池
     */
    SINGLE_POOL,

    /**
     * 分片模式
     */
    SHARDED,

    /**
     * 哨兵模式集群
     */
    SENTINEL,

    /**
     * 集群
     */
    CLUSTER

}
