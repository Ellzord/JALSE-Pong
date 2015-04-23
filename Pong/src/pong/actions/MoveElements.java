package pong.actions;

import static jalse.attributes.Attributes.newTypeOf;
import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.entities.Entity;

import java.awt.Dimension;
import java.awt.Point;

import pong.entities.Paddle;
import pong.entities.Table;

public class MoveElements implements Action<Entity> {

    private static int inBounds(final int value, final int min, final int max) {
	int newValue = value;
	if (newValue < min) {
	    newValue = min;
	}
	if (newValue > max) {
	    newValue = max;
	}
	return newValue;
    }

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Table table = context.getActor().asType(Table.class);
	final Dimension tableSize = table.getSize();
	// Move all elements
	table.streamElements().forEach(element -> {
	    final Point position = element.getPosition();
	    final Point moveDelta = element.getMoveDelta();
	    final Dimension elementSize = element.getSize();

	    final int x = inBounds(position.x + moveDelta.x, 0, tableSize.width - elementSize.width);
	    int y = position.y + moveDelta.y;
	    if (element.isMarkedAsType(Paddle.class)) {
		// Keep paddles in y bounds
		y = inBounds(y, 0, tableSize.height - elementSize.height);
	    }

	    final Point newPosition = new Point(x, y);
	    System.out.println(newPosition);

	    // Check position changed
	    if (!newPosition.equals(position)) {
		position.setLocation(newPosition);
		// Signal change
		element.fireAttributeChanged("position", newTypeOf(Point.class));
	    }
	});
    }
}
