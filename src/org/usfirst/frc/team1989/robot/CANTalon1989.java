package org.usfirst.frc.team1989.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class CANTalon1989 extends CANTalon {
	public double maxI = 0.0; 
	public double lasttimer;
	public Timer t1= new Timer();
	public double factor = 1.0;
	private double dSpeed = 0;
	public int overcurrent = 0;
	
	public CANTalon1989(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
		super(deviceNumber, controlPeriodMs, enablePeriodMs);
		// TODO Auto-generated constructor stub
		t1.start();
		this.overcurrent = 0;
		lasttimer = t1.get();
	}

	public CANTalon1989(int deviceNumber, int controlPeriodMs) {
		super(deviceNumber, controlPeriodMs);
		// TODO Auto-generated constructor stub
		this.overcurrent = 0;
		t1.start();
		lasttimer = t1.get();
	}

	public CANTalon1989(int deviceNumber) {
		super(deviceNumber);
		// TODO Auto-generated constructor stub
		t1.start();
		this.overcurrent = 0;
		lasttimer = t1.get();
	}

	public void set(double speed)
	{
		this.dSpeed = speed;
		
		if (this.maxI > 0 && t1.get()> lasttimer +.025)
		{
			double curr = this.getOutputCurrent();
			if (curr > this.maxI || factor<1) 
			{
				this.overcurrent++;
				factor = factor *maxI/curr;
				}
			if( factor > 1 )
			{
				factor = 1 ;
			}
			dSpeed = speed * factor;
			t1.reset();
			lasttimer = t1.get();
		}
		super.set(dSpeed);
	}
	@Override
	public String getSmartDashboardType() {
		// TODO Auto-generated method stub
		return null;
	}

}
