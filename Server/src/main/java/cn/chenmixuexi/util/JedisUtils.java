package cn.chenmixuexi.util;

import cn.chenmixuexi.bean.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 操作redis的工具类
 * @author Administrator
 *
 */
public class JedisUtils {
	/**
	 * redis的连接池
	 */
	private static JedisPool pool = null;
	private static Config config = Config.getConfig();
	static{
		pool = new JedisPool(config.getRedis_ip(), config.getRedis_port());
	}
	
	/**
	 * 从redis连接池获取一条链接
	 * @return
	 */
	public static Jedis getJedis(){
		return pool.getResource();
	}
	
	/**
	 * 把连接归还到redis连接池
	 * @param jedis
	 */
	public static void close(Jedis jedis){
		if(jedis != null){
			jedis.close();
		}
	}

	public static void set(Jedis jedis,String key,Object object){
		byte[] values = serizlize(object);
		jedis.set(key.getBytes(),values);
	}

	public static Object get(Jedis jedis,String key){
		byte[] bytes = jedis.get(key.getBytes());
		if(bytes==null){
			return null;
		}
		return deserialize(bytes);
	}

	/*
	 * 序列化
	 * */
	public static byte[] serizlize(Object object){
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(baos != null){
					baos.close();
				}
				if (oos != null) {
					oos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
	/*
	 * 反序列化
	 * */
	public static Object deserialize(byte[] bytes){
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try{
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
}
