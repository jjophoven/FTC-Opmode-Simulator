package org.firstinspires.ftc.teamcode.fake;

public class SharedGamepad {
    public volatile int keyCode;
    public volatile boolean pressed;

    public synchronized void update(int keyCode, boolean pressed) {
        this.keyCode = keyCode;
        this.pressed = pressed;
    }
}