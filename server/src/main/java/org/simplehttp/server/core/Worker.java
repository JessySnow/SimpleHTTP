package org.simplehttp.server.core;

import lombok.extern.log4j.Log4j2;

import java.net.Socket;
import java.util.function.Function;

/**
 * 工作线程，只负责对请求逻辑的调用
 * 不要在这里写任何的业务逻辑
 */
@Log4j2
public class Worker implements Runnable{

    @Override
    public void run() {
        this.logic.apply(socketIn);
    }


    private Function<Socket, Void> logic;
    private Socket socketIn;

    public Worker(Socket socketIn ,Function<Socket, Void> logic){
        this.socketIn = socketIn;
        this.logic = logic;
    }

}
