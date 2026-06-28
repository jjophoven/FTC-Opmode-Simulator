package org.jjophoven.fakehardware.drivetrain;

import org.jjophoven.fakehardware.FakeMotor;
import org.jjophoven.fit.MotorModel;

public abstract class SimulatedDrivetrain {
    private final FakeMotor[] motors;

    public MotionVector position = new MotionVector(0, 0, 0);
    public MotionVector velocity = new MotionVector(0, 0, 0);
    public MotionVector acceleration = new MotionVector(0, 0, 0);

    protected double[] motorPowers;
    protected double[] motorAccelerations;
    protected double[] motorVelocities;
    protected MotorModel model;
    protected double[] coefficients;
    protected double staticVelocityRegion;
    protected double staticFriction;

    public SimulatedDrivetrain(FakeMotor[] motors, MotorModel model, double[] coefficients, double staticVelocityRegion, double staticFriction) {
        this.motors = motors;

        motorPowers = new double[motors.length];
        motorVelocities = new double[motors.length];
        motorAccelerations = new double[motors.length];
        this.model = model;
        this.coefficients = coefficients;
        this.staticVelocityRegion = staticVelocityRegion;
        this.staticFriction = staticFriction;
    }

    public void step(double deltaTime) {
        boolean allWheelsStationary = true;
        for (int i = 0; i < motors.length; i++) {
            double power = motors[i].getPower();
            double vel = motorVelocities[i];
            double accel = model.predict(coefficients, vel, power, 13);
            // TODO get voltage from sensor

            if (Math.abs(vel) < staticVelocityRegion && Math.abs(accel) < staticFriction) {
                accel = 0;
                vel = 0;
            } else {
                allWheelsStationary = false;
                accel = model.predict(coefficients, vel, power, 13);
                vel += accel * deltaTime;
            }

            motorPowers[i] = power;
            motorVelocities[i] = vel;
            motorAccelerations[i] = accel;
        }

        acceleration = forwardKinematics(motorAccelerations).toFieldFrame(position.theta);

        if (allWheelsStationary) {
            velocity = new MotionVector(0, 0, 0);
        }
        else {
            velocity = velocity.step(acceleration, deltaTime);
        }

        position = position.step(velocity, deltaTime);

        motorVelocities = inverseKinematics(velocity.toRobotFrame(position.theta));

        position.log("Mecanum/pose");
    }

    abstract MotionVector forwardKinematics(double[] motors);
    abstract double[] inverseKinematics(MotionVector motion);
}