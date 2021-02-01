package se.dendromeda.pilampcontroller;

public interface Gpio {

	void blink(int wait);

	boolean checkButton();

	void toggle();
}