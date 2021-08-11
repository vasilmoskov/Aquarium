package aquarium.entities.aquariums;

import aquarium.common.ConstantMessages;
import aquarium.common.ExceptionMessages;
import aquarium.entities.decorations.Decoration;
import aquarium.entities.fish.Fish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseAquarium implements Aquarium {
    private String name;
    private int capacity;
    private Collection<Decoration> decorations;
    private Collection<Fish> fish;

    protected BaseAquarium(String name, int capacity) {
        this.setName(name);
        this.capacity = capacity;
        this.decorations = new ArrayList<>();
        this.fish = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException (ExceptionMessages.AQUARIUM_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int calculateComfort() {
        return this.decorations.stream().mapToInt(Decoration::getComfort).sum();
    }

    @Override
    public void addFish(Fish fish) {
        if (this.fish.size() < this.capacity) {
            this.fish.add(fish);
        } else {
            throw new IllegalStateException(ConstantMessages.NOT_ENOUGH_CAPACITY);
        }
    }

    @Override
    public void removeFish(Fish fish) {
        this.fish.remove(fish);
    }

    @Override
    public Collection<Fish> getFish() {
        return fish;
    }

    @Override
    public void addDecoration(Decoration decoration) {
        this.decorations.add(decoration);
    }

    @Override
    public Collection<Decoration> getDecorations() {
        return decorations;
    }

    @Override
    public void feed() {
        this.fish.forEach(Fish::eat);
    }

    @Override
    public String getInfo() {
        StringBuilder builder = new StringBuilder();

        String fishOutput = fish.isEmpty()
                ? "none"
                : fish.stream().map(Fish::getName).collect(Collectors.joining(" "));

        return String.format("%s (%s):%n" +
                        "Fish: %s%n" +
                        "Decorations: %d%n" +
                        "Comfort: %d",
                name, this.getClass().getSimpleName(), fishOutput, decorations.size(), calculateComfort());
    }
}
