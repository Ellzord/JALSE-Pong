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

    @GetAttribute
    Point getMoveDelta();

    @GetAttribute
    Point getPosition();

    @GetAttribute
    Dimension getSize();

    @GetAttribute
    Integer getSpeedIncrement();

    @SetAttribute
    void setMoveDelta(Point delta);

    @SetAttribute
    void setPosition(Point position);

    @SetAttribute
    void setSize(Dimension size);

    @SetAttribute
    void setSpeedIncrement(Integer speed);

    default void stopMoving() {
	setMoveDelta(new Point(0, 0));
    }
}
