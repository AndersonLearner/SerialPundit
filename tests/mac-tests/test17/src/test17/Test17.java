package test17;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;
import com.embeddedunveiled.serial.ISerialComDataListener;
import com.embeddedunveiled.serial.SerialComDataEvent;

class Data implements ISerialComDataListener{
	@Override
	public void onNewSerialDataAvailable(SerialComDataEvent data) {
		System.out.println("Read from serial port : " + new String(data.getDataBytes()) + "\n");
	}
}

public class Test17 {
	public static void main(String[] args) {
		
		long handle = 0;
		SerialComManager scm = new SerialComManager();
		
		// instantiate class which is will implement ISerialComDataListener interface
		Data dataListener = new Data();
		
		try {
			// open and configure port that will listen data
			handle = scm.openComPort("/dev/ttyUSB1", true, true, false);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			// register data listener for this port
			System.out.println("1" + scm.registerDataListener(handle, dataListener));
			
			// open and configure port which will send data
			long handle1 = scm.openComPort("/dev/ttyUSB0", true, true, false);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle1, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			// wait for data to be displayed on console
			scm.writeString(handle1, "test", 0);
			Thread.sleep(100);
			System.out.println("2" + scm.unregisterDataListener(dataListener));

			Thread.sleep(100);
			System.out.println("3" + scm.registerDataListener(handle, dataListener));

			scm.writeString(handle1, "test string", 0);
			System.out.println("4" + scm.unregisterDataListener(dataListener));
			
			Thread.sleep(100);
			System.out.println("5" + scm.registerDataListener(handle, dataListener));

			scm.writeString(handle1, "test string", 0);
			System.out.println("6" + scm.unregisterDataListener(dataListener));
			Thread.sleep(100);
			
			// close the port releasing handle
			scm.closeComPort(handle);
			scm.closeComPort(handle1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}