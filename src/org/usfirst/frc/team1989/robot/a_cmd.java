package org.usfirst.frc.team1989.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public abstract class a_cmd extends IterativeRobot implements cmd {
	
	// Not sure this is needed - will talk to Mr. Pirringer about it
	public String type = ""; // holds class type
	public static double Kp = 0.03; // const for multiplying gyro angle 
	
	// Instantiating TalonSRX Motors
	CANTalon1989 frontLeftMotor = new CANTalon1989(3);
	CANTalon1989 frontRightMotor = new CANTalon1989(9);
	CANTalon1989 rearLeftMotor = new CANTalon1989(6);
	CANTalon1989 rearRightMotor = new CANTalon1989(7);
	CANTalon1989 shootMotor1 = new CANTalon1989(2);
	CANTalon1989 shootMotor2 = new CANTalon1989(8);
	CANTalon1989 armMotor1 = new CANTalon1989(1);
	CANTalon1989 armMotor2 = new CANTalon1989(4);
	CANTalon1989 elevator = new CANTalon1989(5);
	ADXRS450_Gyro gyro;
//	AnalogInput rf1 = new AnalogInput(0);
	Accelerometer b_acc;
	JsScaled driveStick = new JsScaled(0);
	JsScaled uStick = new JsScaled(1);//The uStick will stand for the utility joystick responsible for shooting and arm movement
	int autoStatus = 0;
	int autoMode = 0;
	
	// Needs to be updated when hooked up.
//	public RangeFinderCmd rangeFinder = new RangeFinderCmd(0);
	
	/**
	 * For the disabledInit and disabledPeriodic, be sure to disable all motors.
	 * Whenever a motor is added, it should be done in this class.
	 * Once a motor is initiated, it should also be added to the disabled method below.
	 */
	public void disabledInit(){
	}
	
    public void disabledPeriodic(){
    	// Disable all motors
    	frontLeftMotor.set(0);
    	frontRightMotor.set(0);
    	rearLeftMotor.set(0);
    	rearRightMotor.set(0);
    	shootMotor1.set(0);
    	shootMotor2.set(0);
    	armMotor1.set(0);
    	armMotor2.set(0);
    	elevator.set(0);
    }

  //public ArrayList<autocmd> auto_list = new ArrayList<autocmd>(); // list of auto commands

  	/*public void addauto(String Command)
  	{
  		this.auto_list.add(new autocmd(Command));
  	}*/

	
}
