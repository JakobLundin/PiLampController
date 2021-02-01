package se.dendromeda.pilampcontroller;

import org.springframework.stereotype.Component;

@Component
public class GpioMock implements Gpio {

	@Override
	public void blink(int wait) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkButton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub

	}

}
