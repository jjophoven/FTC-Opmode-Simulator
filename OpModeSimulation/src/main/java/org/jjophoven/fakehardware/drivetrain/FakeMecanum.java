package org.jjophoven.fakehardware.drivetrain;

import org.jjophoven.fakehardware.FakeMotor;
import org.jjophoven.fit.MotorModel;

public class FakeMecanum extends SimulatedDrivetrain {
    private static final int FL = 0;
    private static final int BL = 1;
    private static final int FR = 2;
    private static final int BR = 3;

    private final double R;

    public FakeMecanum(FakeMotor[] motors, double[] coefficients, double staticVelocityRegion, double staticFriction, double wheelbase, double trackWidth) {
        super(motors, MotorModel.fromString("a=Au-Bv*abs(d)-Cv-Dsgn(v)"), coefficients, staticVelocityRegion, staticFriction);

        R = wheelbase + trackWidth;
    }

    MotionVector forwardKinematics(double[] motors) {
        double fl = motors[FL];
        double fr = motors[FR];
        double bl = motors[BL];
        double br = motors[BR];

        return new MotionVector(
            (fl + fr + bl + br) / 4,
                (fl - fr + br - bl) / 4,
                -(br - bl + fr - fl) / (4 * R)
        );
    }

    double[] inverseKinematics(MotionVector motion) {
        return new double[]{
                motion.x + motion.y + motion.theta * R,
                motion.x - motion.y + motion.theta * R,
                motion.x - motion.y - motion.theta * R,
                motion.x + motion.y - motion.theta * R
        };
    };
}