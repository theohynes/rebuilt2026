# rebuilt2026_v1_2_18_26
Welcome to the first version of the code for Rebuilt

2/25/2026-TJ
-Arm is not ready yet so when it actually is:
-->Change armSubsystem from TXT to JAVA 
-->Change POVArmMotorCommand from TXT to JAVA
-->RobotContainer 
     22 //@TODO: create arm subsystem and import it here
     23 //import frc.robot.subsystems.armSubsystem;
     24 //import frc.robot.commands.POVArmMotorCommand;
     25 //import frc.robot.commands.extendArmToBar;

     68 //@TODO: create arm subsystem and import it here
     69 //public static final armSubsystem m_armSubsystem = new armSubsystem(); //arm subsystem

     81 //@TODO Will be for the arm, not sure if we need it yet
     82 //private POVArmMotorCommand povMotorCommand;

     122 // WILL BE FOR THE ARM @TODO 
     123 //Left Trigger -> arm moves up
     124 //idy is isdoneyet
     125  /** 
     126  m_driverController.leftTrigger(OIConstants.kTriggerButtonThreshold)
     127  .whileTrue(new extendArmToBar(m_armSubsystem, 5, 0.5, false));
     128  **/

-->Constants
     138 //@TODO: create arm subsystem and add constants here
     139 // Arm options
     140 //public static final boolean ARM_MOTOR_INVERTED = false;
     141 //public static final int ARM_MOTOR_CAN_ID = 31;

2/18/2026-TJ
Things I did so far (and it's a lot :")  
-> set up the base code from the Swerve drive code  
--> Translate any and all FRC Kitbot Code to be REV friendly
    --adjust constants to match:  
          ~length/width of robot    
          ~RPM of vortex motors  
          ~assigned numbers of Sparkmaxes (10s for swerves/20s for intake and launcher/30s for arm)  
    --set up controller in robotContainer  
        ~left is going to control arm  
        ~right controls launcher  
    --update imports in all classes to match most recent non-depricated libraries  
    --updated the gyro so it is recognized as a pigeon rather than the ADIS IMU  
    --added the FuelSubsystem and armSubsystems and all necessary methods  
    --added commands that run independently of the subsystems (You pick and choose which ones to call on based on the buttons)  
        ~I need to finish POVArmMotorCommand which is a more accurate version of extendArmToBar and seems like it will be used with a camera if we add that  
    --pause created to give motors a min to breathe when switching directions  
    --extendArmtoBar is actually mostly my own code and cycles the robot's arm 12 seconds for now to extend the arm to "full height" and then pause 2 seconds and then reverse the motor so it pulls itself up. Obviously we can adjust the speeds as necessary'  
    --Created 2 examples of autos not including the classic S one that Rev loves sneaking in
        ~1st is just a drive away auto
        ~2nd is drive and shoot for 9 seconds 
