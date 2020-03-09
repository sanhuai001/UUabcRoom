package com.uuabc.classroomlib.builder;


import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * 建造者设计模式写的socketIO的生成类
 * Created by bobi on 2018/4/17.
 */

public class SocketIoBuild {

    public static final class Builder {
        private IO.Options options = new IO.Options();
        private String url;

        /**
         * 超时时间
         *
         * @param timeout
         * @return
         */
        public Builder timeout(long timeout) {
            options.timeout = timeout;
            return this;
        }

        /**
         * 是否重连
         *
         * @param reconnection
         * @return
         */
        public Builder reconnection(boolean reconnection) {
            options.reconnection = reconnection;
            return this;
        }

        /**
         * 重连次数
         *
         * @param reconnectionAttempts
         * @return
         */
        public Builder reconnectionAttempts(int reconnectionAttempts) {
            options.reconnectionAttempts = reconnectionAttempts;
            return this;
        }

        /**
         * 延迟重连时间
         *
         * @param reconnectionDelay
         * @return
         */
        public Builder reconnectionDelay(long reconnectionDelay) {
            options.reconnectionDelay = reconnectionDelay;
            return this;
        }

        public Builder reconnectionDelayMax(long reconnectionDelayMax) {
            options.reconnectionDelayMax = reconnectionDelayMax;
            return this;
        }

        /**
         * 是否强制
         *
         * @param forceNew
         * @return
         */
        public Builder forceNew(boolean forceNew) {
            options.forceNew = forceNew;
            return this;
        }

        public Builder transports(String[] transports) {
            options.transports = transports;
            return this;
        }

        public Builder path(String path) {
            options.path = path;
            return this;
        }

        public Builder query(String query) {
            options.query = query;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Socket build() {
            Socket socket = null;
            try {
                socket = IO.socket(checkProtocol(url), options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return socket;
        }
    }

    public static String checkProtocol(String url) {
        if (url.startsWith("wss")) {
            return url.replace("wss", "https");
        }
        return url;
    }
}
