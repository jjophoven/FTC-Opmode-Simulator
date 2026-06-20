//package org.firstinspires.ftc.teamcode.fake;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//
//
//public class Receiver {
//
//    private static final int PORT = 8080;
//    private static final int SOCKET_TIMEOUT_MS = 50;
//
//    private final org.firstinspires.ftc.teamcode.fake.SharedGamepad sharedGamepad;
//
//    private ServerSocket serverSocket;
//    private Socket socket;
//    private DataInputStream in;
//    private Process process;
//
//    public boolean running = false;
//    private boolean initialized = false;
//
//    public Receiver(org.firstinspires.ftc.teamcode.fake.SharedGamepad sharedGamepad) {
//        this.sharedGamepad = sharedGamepad;
//    }
//
//    public void start() throws IOException {
//        log("Starting receiver on port " + PORT);
//
//        serverSocket = new ServerSocket(PORT);
//        serverSocket.setSoTimeout(SOCKET_TIMEOUT_MS);
//
//        process = startInputProcess();
//
//        log("Waiting for connection...");
//        running = true;
//        initialized = false;
//    }
//
//    // ---- 2. UPDATE LOOP (call repeatedly) ----
//    public void update() throws IOException {
//        if (!running) return;
//
//        try {
//            // Step A: accept connection once
//            if (!initialized) {
//                try {
//                    socket = serverSocket.accept();
//                    in = new DataInputStream(socket.getInputStream());
//
//                    log("input connected");
//                    initialized = true;
//                } catch (SocketTimeoutException ignored) {
//                    return; // keep polling
//                }
//            }
//
//            // Step B: read input iteratively (non-blocking style)
//            while (in.available() >= 5) {
//                int keyCode = in.readInt();
//                boolean pressed = in.readBoolean();
//
//                sharedGamepad.update(keyCode, pressed);
//
//                log("keyCode=" + keyCode + ", " + (pressed ? "pressed" : "released"));
//            }
//
//        } catch (IOException e) {
//            shutdown();
//            throw e;
//        }
//    }
//
//    // ---- 3. STOP ----
//    public void shutdown() {
//        running = false;
//
//        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
//        try { if (serverSocket != null) serverSocket.close(); } catch (Exception ignored) {}
//
//        if (process != null) {
//            process.destroy();
//            log("Input process stopped");
//        }
//
//        log("org.firstinspires.ftc.teamcode.fake.Receiver shutdown");
//    }
//
//    // ---- PROCESS START ----
//    private static Process startInputProcess() throws IOException {
//        File projectRoot = findProjectRoot();
//        File gradlew = new File(projectRoot, isWindows() ? "gradlew.bat" : "gradlew");
//
//        log("Starting input process");
//        log("Project root: " + projectRoot.getAbsolutePath());
//
//        return new ProcessBuilder(
//                gradlew.getAbsolutePath(),
//                ":Simulation:run"
//        )
//                .directory(projectRoot)
//                .redirectErrorStream(true)
//                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
//                .start();
//    }
//
//    // ---- ROOT FINDER ----
//    private static File findProjectRoot() throws IOException {
//        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();
//
//        while (dir != null) {
//            File gradlew = new File(dir, isWindows() ? "gradlew.bat" : "gradlew");
//            File settingsGradle = new File(dir, "settings.gradle");
//
//            if (gradlew.isFile() && settingsGradle.isFile()) {
//                return dir;
//            }
//
//            dir = dir.getParentFile();
//        }
//
//        throw new IOException("Could not find project root");
//    }
//
//    private static boolean isWindows() {
//        return System.getProperty("os.name").toLowerCase().contains("win");
//    }
//
//    private static void log(String message) {
//        System.out.println("[org.firstinspires.ftc.teamcode.fake.Receiver] " + message);
//    }
//}