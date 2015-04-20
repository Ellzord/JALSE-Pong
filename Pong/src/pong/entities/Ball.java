package pong.entities;

import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface Ball extends TableElement {

    enum Direction {

	RIGHT, LEFT;
    }

    @GetAttribute("direction")
    Direction getDirection();

    @GetAttribute("hits")
    Integer getHits();

    @SetAttribute("direction")
    void setDirection(Direction direction);

    @SetAttribute("hits")
    void setHits(Integer hits);
}
