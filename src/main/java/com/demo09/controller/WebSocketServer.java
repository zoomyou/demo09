package com.demo09.controller;

import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import cn.hutool.log.Log;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {

    /** 打印信息的对象 */
    private static Log log = LogFactory.get(WebSocketServer.class);

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;

    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    public static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    /**接收userId*/
    private String userId="";
    /**接收password*/

    /**
     *  用于请求和服务器连接
     * @param session 会话连接对象
     * @param userId 连接用户的id信息
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)){
            //如果之前已存在连接就将之前的连接移除重新建立并加入到映射中
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }

        log.info("id为："+userId+"用户连接"+"，当前在线人数为："+getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户："+userId+"，网络异常！！！！！！");
        }
    }

    @OnClose
    public void onClose(){
        if (webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出："+userId+"，当前在线人数为："+getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人（防止篡改）
                //jsonObject.put("fromUserId",this.userId);
                String toUserId = jsonObject.getString("toId");
                //传送给对应的toUserId用户的webSocket
                if (StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else {
                    log.error("请求的userId:"+toUserId+"不在该服务器上");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 获取当前在线用户数量
     * @return 当前在线用户数量
     */
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }

    /**
     * 当前用户数量加一
     */
    public static synchronized void addOnlineCount(){
        WebSocketServer.onlineCount ++ ;
    }

    /**
     * 当前用户数量减一
     */
    public static synchronized void subOnlineCount(){
        WebSocketServer.onlineCount -- ;
    }
}
