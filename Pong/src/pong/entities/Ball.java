package pong.entities;

import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

import java.awt.Point;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public interface Ball extends TableElement {

    UUID ID = UUID.randomUUID();

    @GetAttribute("maxSpeed")
    Integer getMaxSpeed();

    default void randomMoveLeft() {
	final int speed = getSpeed();
	setMoveDelta(new Point(-speed, ThreadLocalRandom.current().nextBoolean() ? -speed : speed));
    }

    default void randomMoveRight() {
	final int speed = getSpeed();
	setMoveDelta(new Point(speed, ThreadLocalRandom.current().nextBoolean() ? -speed : speed));
    }

    @SetAttribute("maxSpeed")
    void setMaxSpeed(Integer maxSpeed);
}
