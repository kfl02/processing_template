package verlet;

import java.util.Set;
import java.util.HashSet;

public class World {
	Set<Entity> entities = new HashSet<Entity>();

	void setEntities(Set<Entity> entities) {
		this.entities = entities;
	}

	void addEntities(Set<Entity> entities) {
		this.entities.addAll(entities);
	}

	Entity addEntity(Entity entity) {
		entities.add(entity);

		return entity;
	}

	public void update() {
		for(Entity e : entities) {
			e.update();
		}
	}

	public void draw() {
		for(Entity e : entities) {
			e.draw();
		}
	}
}
