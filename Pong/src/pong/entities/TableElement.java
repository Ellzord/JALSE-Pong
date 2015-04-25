package pong.entities;

import static jalse.attributes.Attributes.newNamedTypeOf;
import jalse.attributes.NamedAttributeType;
import jalse.entities.Entity;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

import java.awt.Dimension;
import java.awt.Point;

public interface TableElement extends Entity {

    NamedAttributeType<Point> POSITION_TYPE = newNamedTypeOf("position", Point.class);

    @GetAttribute("moveDelta")
    Point getMoveDelta();

    @GetAttribute("position")
    Point getPosition();

    @GetAttribute("size")
    Dimension getSize();

    @GetAttribute("speedIncrement")
    Integer getSpeedIncrement();

    @SetAttribute("moveDelta")
    void setMoveDelta(Point delta);

    @SetAttribute("position")
    void setPosition(Point position);

    @SetAttribute("size")
    void setSize(Dimension size);

    @SetAttribute("speedIncrement")
    void setSpeedIncrement(Integer speed);

    default void stopMoving() {
	setMoveDelta(new Point(0, 0));
    }
}
