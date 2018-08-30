import com.ivan.xinput.XInputAxes;
import com.ivan.xinput.XInputButtons;
import com.ivan.xinput.XInputComponents;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.XInputDevice14;
import com.ivan.xinput.enums.XInputAxis;
import com.ivan.xinput.enums.XInputButton;
import com.ivan.xinput.exceptions.XInputNotLoadedException;
import com.ivan.xinput.natives.XInputNatives;

public class Controller {

	public static void main(String[] args) {
		if (XInputDevice.isAvailable()) {
		    System.out.println("XInput 1.3 is available on this platform.");
		}
		
		XInputDevice device = null;
		if(XInputNatives.isLoaded())
			System.out.println("DLL loaded");
		try {
			XInputDevice.getAllDevices();
			device = XInputDevice.getDeviceFor(0);
		} catch (XInputNotLoadedException e1) {
			e1.printStackTrace();
		}
		
	    XInputComponents components = device.getComponents();
		XInputButtons buttons = components.getButtons();
	    XInputAxes axes = components.getAxes();
	    while(device.poll()) {
	    	int[] signals = {0,0,0,0,0};
	    	if(Math.abs(round(axes.get(XInputAxis.RIGHT_THUMBSTICK_Y),1)) >= 0.5) {
	    		signals[4] += round(axes.get(XInputAxis.RIGHT_THUMBSTICK_Y),1) * 1000;
	    	}
	    	if(Math.abs(round(axes.get(XInputAxis.LEFT_THUMBSTICK_Y),1)) >= 0.5) {
	    		signals[3] += round(axes.get(XInputAxis.LEFT_THUMBSTICK_Y),1) * 1000;
	    	}
	    	if(Math.abs(round(axes.get(XInputAxis.RIGHT_THUMBSTICK_X),1)) >= 0.5) {
	    		signals[2] += round(axes.get(XInputAxis.RIGHT_THUMBSTICK_X),1) * 1000;
	    	}
	    	if(Math.abs(round(axes.get(XInputAxis.LEFT_THUMBSTICK_X),1)) >= 0.5) {
	    		signals[1] += round(axes.get(XInputAxis.LEFT_THUMBSTICK_X),1) * 1000;
	    	}
	    	if (buttons.left) {
	    		signals[0] -= 500;
	    	}
	    	if (buttons.right) {
	    		signals[0] += 500;
	    	}
	    	if(buttons.up) {
	    		System.out.println("UP D PAD");
	    	}
	    	if(buttons.down) {
	    		System.out.println("DOWN D PAD");
	    	}
	    	String newOutput = "";
	    	for(int i : signals) {
	    		newOutput += i + " ";
	    	}
	    	newOutput = newOutput.substring(0,newOutput.length()-1);
	    	System.out.println(newOutput);
	    	if(!newOutput.equals("0 0 0 0 0")) {
	    		vibrate(true,device);
		    	try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	vibrate(false,device);
	    	}
	    	
	    }
		
	}
	
	private static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
	public static void vibrate(boolean on, XInputDevice device) {
		if(on) {
			device.setVibration(20000, 20000);	
		}
		else {
			device.setVibration(0, 0);
		}
	}

}
