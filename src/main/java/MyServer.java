import java.io.*;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {

    private static int count = 64;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(count);

    private Map<String, Map<String, Handler>> handlers = new ConcurrentHashMap<>();

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true)
                threadPool.execute(new ClientHandler(serverSocket.accept(), this));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }

    public void addHandler(String method, String path, Handler handler) {
        Map<String, Handler> methodHandlers = new HashMap<>();
        if (handlers.containsKey(method)) {
            methodHandlers = handlers.get(method);
        }
        methodHandlers.put(path, handler);
        handlers.put(method, methodHandlers);
    }

    public Map<String, Map<String, Handler>> getHandlers() {
        return handlers;
    }
}