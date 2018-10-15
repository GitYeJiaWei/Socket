package view;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyIoHandler extends IoHandlerAdapter {

    private Timer timer;
    private TimerTask timerTask;
    private final static Logger log = LoggerFactory.getLogger(MyIoHandler.class);

    //从端口接受消息，会响应此方法来对消息进行处理
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        String msg = message.toString();
        if("exit".equals(msg)){
            //如果客户端发来exit，则关闭该连接
            session.closeNow();
        }
        System.out.print("服务器接受消息成功..."+msg+"\n");
        //发送信息到客户端
        stoptime();
        starttime(session);
        super.messageReceived(session, message);
    }

    //向客服端发送消息后会调用此方法
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.print("服务器发送消息成功...\n");
        //session.closeNow();
        super.messageSent(session, message);
    }

    //关闭与客户端的连接时会调用此方法
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.print("服务器与客户端断开连接...\n");
        super.sessionClosed(session);
    }

    //服务器与客户端创建连接
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.print("服务器与客户端创建连接...\n");
        super.sessionCreated(session);
    }

    //服务器与客户端连接打开
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.print("服务器与客户端连接打开...\n");
        super.sessionOpened(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.print("服务器进入空闲状态...\n");
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        System.out.print("服务器发送异常...\n");
        super.exceptionCaught(session, cause);
    }

    //开始时调用
    private void starttime(IoSession session){
        if (timer==null){
            timer =new Timer();
        }
        if (timerTask == null){
            timerTask =new TimerTask() {
                @Override
                public void run() {
                    SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    Date date =new Date();
                    //向客户端发送消息
                    session.write(date);

                    System.out.print(format.format(date)+"\n");
                }
            };
        }
        timer.schedule(timerTask,1000,1000*5);
    }

    //结束时调用
    private void stoptime(){
        if (timer!=null){
            timer.cancel();
            timer =null;
        }
        if (timerTask!=null){
            timerTask.cancel();
            timerTask =null;
        }
    }
}
