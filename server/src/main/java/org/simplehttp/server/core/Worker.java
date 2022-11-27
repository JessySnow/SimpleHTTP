package org.simplehttp.server.core;

import lombok.extern.log4j.Log4j2;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Function;

/**
 * 工作线程，负责每一个到来请求的处理
 */
@Log4j2
public class Worker implements Runnable{

    @Override
    public void run() {
        this.logic.apply(socketIn);
    }

    private void cleanUp(Closeable ... objects){
        for (Closeable o : objects){
            try {
                o.close();
            } catch (IOException ignored) {}
        }
    }

    private Function<Socket, Void> logic;
    private Socket socketIn;

    public Worker(Socket socketIn ,Function<Socket, Void> logic){
        this.socketIn = socketIn;
        this.logic = logic;
    }

}
