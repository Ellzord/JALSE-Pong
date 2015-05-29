package pong.entities;

import jalse.entities.Entity;
import jalse.entities.annotations.EntityID;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.GetEntity;
import jalse.entities.annotations.NewEntity;
import jalse.entities.annotations.SetAttribute;
import jalse.entities.annotations.StreamEntities;

import java.awt.Dimension;
import java.util.UUID;
import java.util.stream.Stream;

public interface Table extends Entity {

    UUID ID = UUID.randomUUID();

    @EntityID(mostSigBits = 0, leastSigBits = 1)
    @GetEntity
    Ball getBall();

    @EntityID(mostSigBits = 0, leastSigBits = 2)
    @GetEntity
    Paddle getLeftPaddle();

    @EntityID(mostSigBits = 0, leastSigBits = 3)
    @GetEntity
    Paddle getRightPaddle();

    @GetAttribute
    Dimension getSize();

    @EntityID(mostSigBits = 0, leastSigBits = 1)
    @NewEntity
    Ball newBall();

    @EntityID(mostSigBits = 0, leastSigBits = 2)
    @NewEntity
    Paddle newLeftPaddle();

    @EntityID(mostSigBits = 0, leastSigBits = 3)
    @NewEntity
    Paddle newRightPaddle();

    @SetAttribute
    void setSize(Dimension size);

    @StreamEntities
    Stream<TableElement> streamElements();
}
