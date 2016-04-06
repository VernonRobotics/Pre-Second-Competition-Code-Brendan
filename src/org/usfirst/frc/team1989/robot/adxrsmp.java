package org.usfirst.frc.team1989.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

public class adxrsmp extends ADXRS450_Gyro {
	  private SPI m_spi;

	public adxrsmp(Port port)
	{
		

	    m_spi = new SPI(port);
	    m_spi.setClockRate(3000000);
	    m_spi.setMSBFirst();
	    m_spi.setSampleDataOnRising();
	    m_spi.setClockActiveHigh();
	    m_spi.setChipSelectActiveLow();

	    for (int i = 0; i<=14; i++)
	    {
	    	int x =readRegister(0x0c);
	    	System.out.println("reg "+ i + "   " + x);
	    }
//	      DriverStation.reportError("foud................ " +x, false);

	}
	  private int readRegister(int reg) {
		    int cmdhi = 0x8000 | (reg << 1);
		    boolean parity = calcParity(cmdhi);

		    ByteBuffer buf = ByteBuffer.allocateDirect(4);
		    buf.order(ByteOrder.BIG_ENDIAN);
		    buf.put(0, (byte) (cmdhi >> 8));
		    buf.put(1, (byte) (cmdhi & 0xff));
		    buf.put(2, (byte) 0);
		    buf.put(3, (byte) (parity ? 0 : 1));

		    m_spi.write(buf, 4);
		    m_spi.read(false, buf, 4);

		    if ((buf.get(0) & 0xe0) == 0) {
		    	System.out.println("buf err " + buf.get(0) + " " + buf.get(1) + " " + buf.get(2) + " " + buf.get(3) + " ");
		      return 0;  // error, return 0
		    }
		    return (buf.getInt(0) >> 5) & 0xffff;
		  }
	  private boolean calcParity(int v) {
		    boolean parity = false;
		    while (v != 0) {
		      parity = !parity;
		      v = v & (v - 1);
		    }
		    return parity;
		  }

}
