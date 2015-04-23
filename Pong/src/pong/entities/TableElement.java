package pong.entities;

import jalse.entities.Entity;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

import java.awt.Dimension;
import java.awt.Point;

public interface TableElement extends Entity {

    @GetAttribute("moveDelta")
    Point getMoveDelta();

    @GetAttribute("position")
    Point getPosition();

    @GetAttribute("size")
    Dimension getSize();

    @GetAttribute("speed")
    Integer getSpeed();

    @SetAttribute("moveDelta")
    void setMoveDelta(Point delta);

    @SetAttribute("position")
    void setPosition(Point position);

    @SetAttribute("size")
    void setSize(Dimension size);

    @SetAttribute("speed")
    void setSpeed(Integer speed);

    default void stopMoving() {
	setMoveDelta(new Point(0, 0));
    }
}
