// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.util.Units;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.FuelConstants.*;


public class FuelSubsystem extends SubsystemBase {
    @SuppressWarnings("is never used")

  private final SparkMax feederRoller;
  private final SparkMax intakeLauncherRoller;

  /** Creates a new CANBallSubsystem. */
  @SuppressWarnings("removal")
  public FuelSubsystem() {
    // create brushed motors for each of the motors on the launcher mechanism
    //OG CIMS
   // intakeLauncherRoller = new SparkMax(INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushed);
    //feederRoller = new SparkMax(FEEDER_MOTOR_ID, MotorType.kBrushed);

    intakeLauncherRoller = new SparkMax(INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushless);
    feederRoller = new SparkMax(FEEDER_MOTOR_ID, MotorType.kBrushless);

    // put default values for various fuel operations onto the dashboard
    // all methods in this subsystem pull their values from the dashbaord to allow
    // you to tune the values easily, and then replace the values in Constants.java
    // with your new values. For more information, see the Software Guide.
    SmartDashboard.putNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Intaking intake roller value", INTAKING_INTAKE_VOLTAGE);
    SmartDashboard.putNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Launching launcher roller value", LAUNCHING_LAUNCHER_VOLTAGE);
    SmartDashboard.putNumber("Spin-up feeder roller value", SPIN_UP_FEEDER_VOLTAGE);

    // create the configuration for the feeder roller, set a current limit and apply
    // the config to the controller
    SparkMaxConfig feederConfig = new SparkMaxConfig();
    feederConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(50);
    feederConfig.inverted(false);
    feederConfig.smartCurrentLimit(FEEDER_MOTOR_CURRENT_LIMIT);
    feederRoller.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // create the configuration for the launcher roller, set a current limit, set
    // the motor to inverted so that positive values are used for both intaking and
    // launching, and apply the config to the controller
    SparkMaxConfig launcherConfig = new SparkMaxConfig();
    launcherConfig.inverted(false);//originally true for cim
    launcherConfig.smartCurrentLimit(LAUNCHER_MOTOR_CURRENT_LIMIT);
    launcherConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(50);
    intakeLauncherRoller.configure(launcherConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    launcherConfig.voltageCompensation(11.0); // This tells the motor controller that "100% power" equals 11 Volts.
  }

  // A method to set the rollers to values for intaking
  public void intake() {
    feederRoller.setVoltage(SmartDashboard.getNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE));
    intakeLauncherRoller
        .setVoltage(SmartDashboard.getNumber("Intaking intake roller value", INTAKING_INTAKE_VOLTAGE));
  }

  // A method to set the rollers to values for ejecting fuel out the intake. Uses
  // the same values as intaking, but in the opposite direction.
  public void yeetEject() {
    feederRoller
        .setVoltage(-1 * SmartDashboard.getNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE));
    intakeLauncherRoller
        .setVoltage(1 * SmartDashboard.getNumber("Intaking launcher roller value", INTAKING_INTAKE_VOLTAGE));
  }
    // A method to set the rollers to values for launching.
  public void yeetLaunch() {
        feederRoller.setInverted(true);

    feederRoller.setVoltage(SmartDashboard.getNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE));
    intakeLauncherRoller
        .setVoltage(-1*SmartDashboard.getNumber("Launching launcher roller value", -LAUNCHING_LAUNCHER_VOLTAGE));
  }
  //------------------------------------------------------------------------------------------------------------
// A method to set the rollers to values for ejecting fuel out the intake. Uses
  // the same values as intaking, but in the opposite direction.
  public void normalEject() {
    feederRoller
        .setVoltage(.95 * SmartDashboard.getNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE));
    intakeLauncherRoller
        .setVoltage(-.95 * SmartDashboard.getNumber("Intaking launcher roller value", -INTAKING_INTAKE_VOLTAGE));
  }
  // A method to set the rollers to values for launching.
  public void normalLaunch() {
    feederRoller.setVoltage(-SmartDashboard.getNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE*.95));
    intakeLauncherRoller
        .setVoltage(SmartDashboard.getNumber("Launching launcher roller value", -LAUNCHING_LAUNCHER_VOLTAGE*.95));
  }

  // A method to stop the rollers
  public void stop() {
    feederRoller.set(0);
    intakeLauncherRoller.set(0);
  }

  // A method to spin up the launcher roller while spinning the feeder roller to
  // push Fuel away from the launcher
  public void spinUp() {
    feederRoller
        .setVoltage(SmartDashboard.getNumber("Spin-up feeder roller value", -SPIN_UP_FEEDER_VOLTAGE));
    intakeLauncherRoller
        .setVoltage(SmartDashboard.getNumber("Launching launcher roller value", -LAUNCHING_LAUNCHER_VOLTAGE));
  }

  // A command factory to turn the spinUp method into a command that requires this
  // subsystem
  public Command spinUpCommand() {
    return this.run(() -> spinUp());
  }

  // A command factory to turn the launch method into a command that requires this
  // subsystem
  public Command launchCommand() {
    return this.run(() -> normalLaunch());
  }
public Command yeetTheBallCommand(){
  return this.run(()-> yeetLaunch());
}
//------------------------------------------------------------------------------------------


//-----------------------------------------------------------------------------------------------

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
