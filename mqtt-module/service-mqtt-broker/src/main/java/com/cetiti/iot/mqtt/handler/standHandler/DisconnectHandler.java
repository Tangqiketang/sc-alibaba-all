package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.manager.ClientManager;
import com.cetiti.iot.mqtt.manager.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-06 15:48
 */
@Component
@Slf4j
public class DisconnectHandler {

    public void onDisconnect(ContextBo contextBo) {
        log.debug("接收到disconnect请求:{}", contextBo);
        try{
            if(!contextBo.getConnected()){
                contextBo.getHandlerContext().close();
                return;
            }
            SessionManager.removeContextByClientId(contextBo.getClientId());
            ClientManager.removeClient(contextBo.getClientId());
        }catch (Exception e){

        }

    }
}
