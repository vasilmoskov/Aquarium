package aquarium.entities.fish;

public class SaltwaterFish extends BaseFish {
    private static final int SIZE = 5;

    public SaltwaterFish(String name, String species, double price) {
        super(name, species, price);
        setSize(SIZE);
    }

    @Override
    public void eat() {
        this.setSize(getSize() + 2);
    }
}