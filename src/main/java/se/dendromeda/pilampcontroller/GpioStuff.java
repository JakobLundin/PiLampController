package se.dendromeda.pilampcontroller;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PwmExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.springframework.stereotype.Component;

import com.pi4j.io.gpio.*;

/**
 * <p>
 * This example code demonstrates how to setup a hardware supported PWM pin GpioProvider
 * </p>
 *
 * @author Robert Savage
 */
//@Component
public class GpioStuff implements Gpio{

	GpioController gpio = GpioFactory.getInstance();

    GpioPinDigitalInput buttonInput;

    GpioPinPwmOutput pwm;
    
    boolean lampOn;
    
	
    public GpioStuff() {
      
         pwm = gpio.provisionPwmOutputPin(RaspiPin.GPIO_23);
         buttonInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "button", PinPullResistance.PULL_DOWN);
         
         com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
         com.pi4j.wiringpi.Gpio.pwmSetRange(1000);
         com.pi4j.wiringpi.Gpio.pwmSetClock(100);
         
         lampOn = false;
         
    }
    
	public void blink(int wait) {
    	if (lampOn) {
    		fadeOut(wait);
    		fadeIn(wait);
    	} else {
    		fadeIn(wait);
    		fadeOut(wait);
    	}
    }
    
    private void fadeIn(int wait) {
    	for (int i = 0; i < 1000; i++) {
        	long before = System.nanoTime();
        	long nextTime = before+wait;
        	pwm.setPwm(i);
        	
        	while (System.nanoTime() < nextTime);
        }
    }
    
    private void fadeOut(int wait) {
    	for (int i = 1000; i >= 0; i--) {
        	long before = System.nanoTime();
        	long nextTime = before+wait;
        	pwm.setPwm(i);
        	
        	while (System.nanoTime() < nextTime);
        }
    }
    
    
	public boolean checkButton() {
    	return buttonInput.isHigh();
    }

	public void toggle() {
		lampOn = !lampOn;
		setState(lampOn);
	}
	
	private void setState(boolean on) {
		pwm.setPwm(on ? 1000 : 0);
	}
}