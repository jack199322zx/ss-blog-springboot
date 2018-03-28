package com.sstyle.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss on 2018/2/2.
 */
public class RedisClient {
    private static Logger LOG = LoggerFactory.getLogger(RedisClient.class);
    private static final String host = "127.0.0.1";
    private static final int port = 6379;
    private static final int timeout = 2000;
    private static final int database = 0;
    private static final int maxClients = 50;
    private static JedisPool pool;

    static {
        LOG.debug("redis host: {}, port: {}", host, port);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxClients);
        jedisPoolConfig.setTestOnBorrow(true);

        pool = new JedisPool(jedisPoolConfig, host, port, timeout, null, database);
    }

    private static Jedis jedis() {
        return pool.getResource();
    }

    public static String setex(String key, String value, int seconds) {
        Jedis jedis = jedis();
        try {
            return jedis.setex(key, seconds, value);
        } finally {
            jedis.close();
        }
    }

    public static String get(String key) {
        Jedis jedis = jedis();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public static String getAndDel(String key) {
        Jedis jedis = jedis();
        try {
            Transaction multi = jedis.multi();
            multi.get(key);
            multi.del(key);
            List<Object> result = multi.exec();
            return (String) result.get(0);
        } finally {
            jedis.close();
        }
    }

    public static String setByte(byte[] key, byte[] value, int seconds) {
        Jedis jedis = jedis();
        try {
            return jedis.setex(key, seconds, value);
        } finally {
            jedis.close();
        }
    }

    public static byte[] getByte(byte[] key) {
        Jedis jedis = jedis();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public static byte[] getAndDelByTe(byte[] key) {
        Jedis jedis = jedis();
        try {
            byte[] result = jedis.get(key);
            jedis.del(key);
            return result;
        } finally {
            jedis.close();
        }
    }

    public static Long del(String key) {
        Jedis jedis = jedis();
        try {
            return jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public static List getSimilarKeysAndDel(String preFix, String code) {
        Jedis jedis = jedis();
        List<String> keys = new ArrayList();
        try {
            ScanParams scanParams = new ScanParams();
            scanParams.match(preFix + "*" + code + "*");
            scanParams.count(10);
            String cur = ScanParams.SCAN_POINTER_START;
            do {
                ScanResult<String> scan = jedis.scan(cur, scanParams);
                keys.addAll(scan.getResult());
                cur = scan.getStringCursor();
            } while (!cur.equals(ScanParams.SCAN_POINTER_START));
            keys.forEach(key -> jedis.del(key));
        } finally {
            jedis.close();
        }

        return keys;
    }

    public static void expire(String key, int expireTime) {
        Jedis jedis = jedis();
        try {
            jedis.expire(key, expireTime);
        } finally {
            jedis.close();
        }
    }
}