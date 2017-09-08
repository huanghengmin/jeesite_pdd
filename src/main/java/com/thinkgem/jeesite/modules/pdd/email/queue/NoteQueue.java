package com.thinkgem.jeesite.modules.pdd.email.queue;

import com.thinkgem.jeesite.modules.pdd.email.model.Note;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 邮件队列
 * 创建者 科帮网
 * 创建时间	2017年8月4日
 *
 */
public class NoteQueue {
	 //队列大小
    static final int QUEUE_MAX_SIZE   = 1000;

    static BlockingQueue<Note> blockingQueue = new LinkedBlockingQueue<Note>(QUEUE_MAX_SIZE);

    /**
     * 私有的默认构造子，保证外界无法直接实例化
     */
    private NoteQueue(){};
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
		private  static NoteQueue queue = new NoteQueue();
    }
    //单例队列
    public static NoteQueue getNoteQueue(){
        return SingletonHolder.queue;
    }
    //生产入队
    public  void  produce(Note note) throws InterruptedException {
    	blockingQueue.put(note);
    }
    //消费出队
    public Note consume() throws InterruptedException {
        return blockingQueue.take();
    }
    // 获取队列大小
    public int size() {
        return blockingQueue.size();
    }
}
