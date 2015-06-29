package pong.entities;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface Ball extends TableElement {

    @GetAttribute
    int getMaxSpeed();

    default void randomMoveLeft() {
	final int speed = getSpeedIncrement();
	setMoveDelta(new Point(-(speed * 2), ThreadLocalRandom.current().nextBoolean() ? -speed : speed));
    }

    default void randomMoveRight() {
	final int speedInc = getSpeedIncrement();
	setMoveDelta(new Point(speedInc * 2, ThreadLocalRandom.current().nextBoolean() ? -speedInc : speedInc));
    }

    @SetAttribute
    void setMaxSpeed(int maxSpeed);
}
