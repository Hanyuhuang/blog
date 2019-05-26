package com.hyh.listener.component;

import com.hyh.pojo.Bo.NoticeBo;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/websocket/{id}")
public class WebSocket {

    private static Map<Long, Session> map =  new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam(value = "id") Long id, Session session){
        System.out.println("用户"+id+"加入连接--------");
        map.put(id,session);
    }

    @OnClose
    public void onClose(@PathParam(value = "id") Long id){
        System.out.println("用户"+id+"断开连接--------");
        map.remove(id);
    }

    public void sendMessage(NoticeBo noticeBo){
        System.out.println(map);
        // 当前用户在线
        if (noticeBo !=null && map.containsKey(noticeBo.getReceiver())){
            String message = new String();
            if (noticeBo.getType() == 1){
                message = noticeBo.getSender().getName()+"赞了你的文章，点击查看";
            } else if (noticeBo.getType() == 2){
                message = noticeBo.getSender().getName()+"收藏了你的文章，点击查看";
            } else if (noticeBo.getType() == 3) {
                message = noticeBo.getSender().getName() + "评论了你的文章，点击查看";
            }
            map.get(noticeBo.getReceiver()).getAsyncRemote().sendText(message);
        }
    }
}
