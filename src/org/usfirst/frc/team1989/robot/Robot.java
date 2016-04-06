
//Martin's Code

package org.usfirst.frc.team1989.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * 
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends a_cmd {

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	int state = 0;
	AnalogInput rf1;
	double driveramp = 6.0;

	// Instantiating Timer
	Timer t1 = new Timer();

	// Instantiating Servo
	Servo s1 = new Servo(0);

	// Instantiating Joysticks

	// Instantiating writmessage
	writemessage wmsg = new writemessage();

	// ArcadeDriveCMD Constructor - 4 motors
	ArcadeDriveCmd aDrive = new ArcadeDriveCmd(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor,
			driveStick);

	// WHAT THE HECK IS THIS!!!! NO SUPPORT IN CLASSES!
	// writemessage wmsg = new writemessage();

	// Encoder
		Encoder encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);

	ShooterCmd shooter = new ShooterCmd(driveStick, s1);
	ArmsCmd arms = new ArmsCmd(driveStick);

	// ShooterCmd shooter2 = new ShooterCmd(driveStick, s1);
	// ArmsCmd arms2 = new ArmsCmd(driveStick);

	public void robotInit() {
		CameraServer server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam1");
		System.out.println("i'm Alive");
		// gyro = new adxrsmp(SPI.Port.kOnboardCS0); // try 1,2,3 to find the
		// Gyro
		rf1 = new AnalogInput(3);
		b_acc = new BuiltInAccelerometer();
		// Construct CMD List
		SharedStuff.cmdlist.add(aDrive);
		SharedStuff.cmdlist.add(shooter);
		SharedStuff.cmdlist.add(arms);
		// SharedStuff.cmdlist.add(shooter2);
		// SharedStuff.cmdlist.add(arms2);
		SharedStuff.cmdlist.add(wmsg);
		// SharedStuff.cmdlist.add(wmsg); // sb added last so that other objects
		// can update first

		// Limit Switches- In for now, will be changed to CAN network
		frontLeftMotor.enableLimitSwitch(false, false);
		frontRightMotor.enableLimitSwitch(false, false);
		rearLeftMotor.enableLimitSwitch(false, false);
		rearRightMotor.enableLimitSwitch(false, false);
		shootMotor1.enableLimitSwitch(false, false);
		shootMotor2.enableLimitSwitch(false, false);

		// Voltage Ramps - none for now
		// frontLeftMotor.setVoltageRampRate(driveramp);
		// frontRightMotor.setVoltageRampRate(driveramp);
		// rearLeftMotor.setVoltageRampRate(driveramp);
		// rearRightMotor.setVoltageRampRate(driveramp);

		// add ref to list

	}

	//
	public void autonomousInit() {
		// Output RangeFinder Distance
		// rangeFinder.setDistance();
		frontLeftMotor.maxI = 0;
		frontRightMotor.maxI = 0;
		rearLeftMotor.maxI = 0;
		rearRightMotor.maxI = 0;
		frontLeftMotor.overcurrent = 0;
		frontRightMotor.overcurrent = 0;
		rearLeftMotor.overcurrent = 0;
		rearRightMotor.overcurrent = 0;
		autoStatus = 0;
		autoMode = (SmartDashboard.getBoolean("DB/Button 0") ? 1 : 0)
				+ (SmartDashboard.getBoolean("DB/Button 1") ? 2 : 0)
				+ (SmartDashboard.getBoolean("DB/Button 2") ? 4 : 0)
				+ (SmartDashboard.getBoolean("DB/Button 2") ? 8 : 0);
		SharedStuff.msg[0] = "Automode " + autoMode;
		t1.stop();
		t1.reset();
		t1.start();
		for (int i = 0; i < SharedStuff.cmdlist.size(); i++) {
			SharedStuff.cmdlist.get(i).autonomousInit();
		}

	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		// Output RangeFinder Distance
		// rangeFinder.setDistance()
		SharedStuff.msg[2] ="c " + frontRightMotor.overcurrent + " " + frontRightMotor.factor;

		if (autoMode == 1) {
			autoMode1(false);
		}
		else if(autoMode == 2) {
			autoMode1(true);
		} 
		else if(autoMode == 4) {
			autoMode4(false);
		} 
		for (int i = 0; i < SharedStuff.cmdlist.size(); i++) {
			SharedStuff.cmdlist.get(i).autonomousPeriodic();
		}

	}

	/*
	 * guilotine
	 */
	public void autoMode4(boolean armup) {
		driveStick.buttons[6] = armup;
		driveStick.buttons[4] = !armup;

		if (autoStatus == 0) {
			autoStatus = 1;
			driveStick.pY = .65;
			t1.stop();
			t1.reset();
			t1.start();
		} else if (autoStatus == 1) {
			driveStick.pY = .65;
			if (t1.get() > 2) {
				t1.stop();
				t1.reset();
				t1.start();
				frontLeftMotor.maxI = 4;
				frontRightMotor.maxI = 4;
				rearLeftMotor.maxI = 4;
				rearRightMotor.maxI = 4;
				frontLeftMotor.overcurrent = 0;
				frontRightMotor.overcurrent = 0;
				rearLeftMotor.overcurrent = 0;
				rearRightMotor.overcurrent = 0;
				autoStatus = 2;
			}
		} else if (autoStatus == 2 ){
			if (frontRightMotor.overcurrent > 10 || rearRightMotor.overcurrent > 10 ||frontLeftMotor.overcurrent > 10 || rearLeftMotor.overcurrent > 10)
				{autoStatus = 3;}
			
			driveStick.pY = .65;
			if (t1.get() >5) {
				t1.stop();
				t1.reset();
				t1.start();
				autoStatus = 3;
			}


		} else if (autoStatus == 3) {
			autoStatus = 4;
			driveStick.pY = 0;
		}
		else if (autoStatus == 4) {
			autoStatus = 5;
			armMotor1.set(-.8);
			armMotor2.set(-.8);
			t1.reset();
			t1.stop();
			t1.start();
			driveStick.pY = 0;
		} else if (autoStatus == 5) {
			armMotor1.set(-.5);
			armMotor2.set(-.5);
			if (t1.get() > .5) {
				driveStick.pY = .65;
				autoStatus = 6;
			}
		} else if (autoStatus == 6) {
			armMotor1.set(-.5);
			armMotor2.set(-.5);
			if (t1.get() > 3) {
				t1.reset();
				t1.stop();
				driveStick.pY = 0;
				armMotor1.set(0);
				armMotor2.set(0);
				autoStatus = 7;
			}
		}
	}
	/*
	 * arms up or down go straight
	 */
	public void autoMode1(boolean armup) {
		driveStick.buttons[6] = armup;
		driveStick.buttons[4] = !armup;

		if (autoStatus == 0) {
			autoStatus = 1;
			driveStick.pY = .65;
			t1.stop();
			t1.reset();
			t1.start();
		} else if (autoStatus == 1) {
			driveStick.pY = .65;
			if (t1.get() > 2) {
				t1.stop();
				t1.reset();
				t1.start();
				frontLeftMotor.maxI = 4;
				frontRightMotor.maxI = 4;
				rearLeftMotor.maxI = 4;
				rearRightMotor.maxI = 4;
				frontLeftMotor.overcurrent = 0;
				frontRightMotor.overcurrent = 0;
				rearLeftMotor.overcurrent = 0;
				rearRightMotor.overcurrent = 0;
				autoStatus = 2;
			}
		} else if (autoStatus == 2 ){
			if (frontRightMotor.overcurrent > 10 || rearRightMotor.overcurrent > 10 ||frontLeftMotor.overcurrent > 10 || rearLeftMotor.overcurrent > 10)
				{autoStatus = 3;}
			
			driveStick.pY = .65;
			if (t1.get() >5) {
				t1.stop();
				t1.reset();
				t1.start();
				autoStatus = 3;
			}


		} else if (autoStatus == 3) {
			autoStatus = 3;
			driveStick.pY = 0;
		}
	}

	public void teleopInit() {
		t1.start();
		state = 0;
		frontLeftMotor.maxI = 0;
		frontRightMotor.maxI = 0;
		rearLeftMotor.maxI = 0;
		rearRightMotor.maxI = 0;
	}

	/**
	 * This function is called periodically during operator control
	 */

	public void teleopPeriodic() {
		// gyrotest should display stuff at th display in string 2 on down

		double distance = rf1.getVoltage() * 102.4;
		if (t1.get() > .25) {
			// Double angle = gyro.getAngle();
			Double xVal = b_acc.getX();
			Double yVal = b_acc.getY();
			Double zVal = b_acc.getZ();
			SharedStuff.msg[1] = "RF: " + new Integer((int) distance).toString();
			SharedStuff.msg[3] = " Enc pos " + elevator.getEncPosition();
			// Integer ia = new Integer(angle.intValue()* 100);
			SharedStuff.msg[7] = " x  " + xVal.toString();//
			// SharedStuff.msg[6] = " angle " + angle.toString();
			SharedStuff.msg[8] = " y  " + yVal.toString();
			SharedStuff.msg[9] = " z  " + zVal.toString();

			t1.reset();
			System.out.println("LF" + frontLeftMotor.getOutputCurrent() + " LR "  + rearLeftMotor.getOutputCurrent() +
					" RF " + frontRightMotor.getOutputCurrent() + " RR "  + rearRightMotor.getOutputCurrent());
		}
		// Output RangeFinder Distance
		// rangeFinder.setDistance();
		if (driveStick.getRawButton(7)) {
			state = 1;
		}
		if (state > 0) {
			if (state == 1) {
				state = 2;
				armMotor1.set(-.8);
				armMotor1.set(-.8);
				t1.reset();
				t1.stop();
				t1.start();
				driveStick.pY = 0;
				aDrive.autonomousPeriodic();
			} else if (state == 2) {
				armMotor1.set(-.5);
				armMotor2.set(-.5);
				aDrive.autonomousPeriodic();
				if (t1.get() > .5) {
					driveStick.pY = .65;
					state = 3;
				}
			} else if (state == 3) {
				armMotor1.set(-.5);
				armMotor2.set(-.5);
				aDrive.autonomousPeriodic();
				if (t1.get() > 3) {
					t1.reset();
					t1.stop();
					driveStick.pY = 0;
					armMotor1.set(0);
					armMotor1.set(0);
					state = 0;

				}

			}
		}
		else {
			for (int i = 0; i < SharedStuff.cmdlist.size(); i++) {
				SharedStuff.cmdlist.get(i).teleopPeriodic();
			}
		}
		
		aDrive.aimingMode();
	}

	public void testInit() {
		// Output RangeFinder Distance
		// rangeFinder.setDistance();
		t1.start();
		state = 0;
	}

	/**
	 * This function is called periodically during test mode
	 * 
	 */
	public void testPeriodic() {
		// Output RangeFinder Distance
		// rangeFinder.setDistance();

		if (driveStick.getRawButton(7)) {
			state = 1;
		}
		if (state > 0) {
			if (state == 1) {
				state = 2;
				armMotor1.set(-.8);
				armMotor1.set(-.8);
				t1.reset();
				t1.stop();
				t1.start();
				driveStick.pY = 0;
				aDrive.autonomousPeriodic();
			} else if (state == 2) {
				armMotor1.set(-.5);
				armMotor2.set(-.5);
				aDrive.autonomousPeriodic();
				if (t1.get() > .5) {
					driveStick.pY = .65;
					state = 3;
				}
			} else if (state == 3) {
				armMotor1.set(-.5);
				armMotor2.set(-.5);
				aDrive.autonomousPeriodic();
				if (t1.get() > 3) {
					t1.reset();
					t1.stop();
					driveStick.pY = 0;
					armMotor1.set(0);
					armMotor1.set(0);
					state = 0;

				}

			}
		} else {
			aDrive.testPeriodic();
			// wmsg.testPeriodic();
			// Servo Logic
			if (driveStick.getRawButton(5) == true) {
				s1.set(0);
			} else if (driveStick.getRawButton(6) == true) {
				s1.set(1);
			}

			// Shooting Logic
			if (driveStick.getRawButton(2) == true) {

				// Motors for pickup
				shootMotor1.set(-.35);
				shootMotor2.set(.35);
			} else if (driveStick.getRawButton(1)) {

				// Motors for shoot
				shootMotor1.set(1);
				shootMotor2.set(-1);
			} else {
				shootMotor1.set(0);
				shootMotor2.set(0);
			}

			// Elevator Logic
			if (driveStick.getRawButton(3) == true) {
				elevator.set(.2);
			} else if (driveStick.getRawButton(4)) {
				elevator.set(-.2);
			} else {
				elevator.set(-.05);
			}
			
		
			//Slows robot down to allow more fine tuned aiming
			aDrive.aimingMode();				
			
		// Debug Output
		SharedStuff.led[2] = true;
		SharedStuff.msg[0] = " Left I " + frontLeftMotor.getOutputCurrent();
		SharedStuff.msg[5] = "right I " + frontRightMotor.getOutputCurrent();
		SharedStuff.msg[1] = " Left O " + frontLeftMotor.getOutputVoltage();
		SharedStuff.msg[6] = "right O " + frontRightMotor.getOutputVoltage();
		SharedStuff.msg[2] = " Left V " + frontLeftMotor.getBusVoltage();
		SharedStuff.msg[7] = "right V " + frontRightMotor.getBusVoltage();
		SharedStuff.msg[3] = " Enc pos " + elevator.getEncPosition();
		SharedStuff.msg[8] = "getpos" + elevator.getPosition();
		SharedStuff.msg[4] = " sh1 I " + shootMotor1.getOutputCurrent();
		SharedStuff.msg[9] = "right S " + shootMotor2.getOutputCurrent();
		wmsg.testPeriodic();
	}
	}
}