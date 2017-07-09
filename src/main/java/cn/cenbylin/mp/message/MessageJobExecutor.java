package cn.cenbylin.mp.message;

import cn.cenbylin.mp.common.utils.MessageCreator;
import cn.cenbylin.mp.dev.message.BasicMessageProcessor;
import cn.cenbylin.mp.message.po.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;

/**
 * Created by Cenbylin on 2017/7/9.
 */
public class MessageJobExecutor implements Runnable{
    //任务队列
    private LinkedList<MessageBean> jobQueue;
    //是否阻塞
    public boolean wait;
    @Autowired
    MessageCreator messageCreator;
    @Autowired
    LinkedList messageProcessorList;

    /**
     * 增加一个任务
     * @param msb
     * @return
     */
    public synchronized boolean addJob(MessageBean msb){
        jobQueue.addLast(msb);
        return true;
    }

    /**
     * 获得一个任务
     * @return
     */
    public synchronized MessageBean getJob(){
        if (jobQueue!=null && jobQueue.size()>0){
            MessageBean msb = jobQueue.getFirst();
            jobQueue.removeFirst();
            return msb;
        }
        return null;
    }

    public void run(){
        while (true){
            MessageBean msb = getJob();
            if (msb==null){
                //阻塞等待唤醒，以自己为锁
                synchronized (this) {
                    this.wait = true;
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //开始处理
            process(msb);
        }
    }

    /**
     * 消息任务处理（core）
     * @param msb
     */
    private void process(MessageBean msb){
        /**
         * proccess
         */
        Object res = "";
        if("text".equals(msb.getMsgType())){
            for(Object o:messageProcessorList){
                res = ((BasicMessageProcessor)o)
                        .doText(msb.getFromUserName(), msb.getContent());
            }
        }else if ("url".equals(msb.getMsgType())){
            for(Object o:messageProcessorList){
                res = ((BasicMessageProcessor)o)
                        .doUrl(msb.getFromUserName(), msb.getContent());
            }
        }
    }
}
