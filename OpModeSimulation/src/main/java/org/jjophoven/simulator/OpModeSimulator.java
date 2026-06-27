package org.jjophoven.simulator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.jjophoven.driverstation.OpModeState;
import org.jjophoven.input.Keybinds;

import java.io.IOException;

public class OpModeSimulator {
    public static void simulate(OpMode opMode) throws InterruptedException, IOException {
        DriverStation driverStation = new DriverStation(opMode, new Keybinds(), new Keybinds());

        driverStation.startServer();
        driverStation.acceptClient();

        System.out.println(driverStation.state);

        while (driverStation.state == OpModeState.WAIT_FOR_INIT) {
            driverStation.poll();
            Thread.sleep(20);
        }

        opMode.init();

        while (driverStation.state == OpModeState.INITIALIZING) {
            driverStation.update();

            opMode.init_loop();

            Thread.sleep(20);
        }

        opMode.start();

        while (driverStation.state == OpModeState.RUNNING) {
            driverStation.update();

            opMode.loop();

            Thread.sleep(20);
        }
    }
}