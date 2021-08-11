package aquarium.core;

import aquarium.common.ConstantMessages;
import aquarium.common.ExceptionMessages;
import aquarium.entities.aquariums.Aquarium;
import aquarium.entities.aquariums.FreshwaterAquarium;
import aquarium.entities.aquariums.SaltwaterAquarium;
import aquarium.entities.decorations.Decoration;
import aquarium.entities.decorations.Ornament;
import aquarium.entities.decorations.Plant;
import aquarium.entities.fish.Fish;
import aquarium.entities.fish.FreshwaterFish;
import aquarium.entities.fish.SaltwaterFish;
import aquarium.repositories.DecorationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {
    private DecorationRepository decorationRepository;
    private Collection<Aquarium> aquariums;

    public ControllerImpl() {
        this.decorationRepository = new DecorationRepository();
        this.aquariums = new ArrayList<>();
    }

    @Override
    public String addAquarium(String aquariumType, String aquariumName) {
        Aquarium aquarium = null;

        switch (aquariumType) {
            case "FreshwaterAquarium":
                aquarium = new FreshwaterAquarium(aquariumName);
                break;
            case "SaltwaterAquarium":
                aquarium = new SaltwaterAquarium(aquariumName);
                break;
            default:
                throw new NullPointerException(ExceptionMessages.INVALID_AQUARIUM_TYPE);
        }

        this.aquariums.add(aquarium);

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_AQUARIUM_TYPE, aquariumType);
    }

    @Override
    public String addDecoration(String type) {

        Decoration decoration = null;

        switch (type) {
            case "Ornament":
                decoration = new Ornament();
                break;
            case "Plant":
                decoration = new Plant();
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_DECORATION_TYPE);
        }

        this.decorationRepository.add(decoration);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_DECORATION_TYPE, type);
    }

    @Override
    public String insertDecoration(String aquariumName, String decorationType) {

        Decoration decoration = this.decorationRepository.findByType(decorationType);

        if (decoration == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_DECORATION_FOUND, decorationType));
        }

        //cant use get method of array list because the reference is a collection
        //iterating seem to me not the best solution.

        this.decorationRepository.remove(decoration);

        for (Aquarium aquarium : aquariums) {
            if (aquarium.getName().equals(aquariumName)) {
                aquarium.addDecoration(decoration);
                break;
            }
        }

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_DECORATION_IN_AQUARIUM, decorationType, aquariumName);
    }

    @Override
    public String addFish(String aquariumName, String fishType, String fishName, String fishSpecies, double price) {

        Fish fish = null;

        Aquarium aquarium = getAquarium(aquariumName);

        switch (fishType) {
            case "FreshwaterFish":
                fish = new FreshwaterFish(fishName, fishSpecies, price);

                if (aquarium instanceof FreshwaterAquarium) {
                    aquarium.addFish(fish);
                } else {
                    return ConstantMessages.WATER_NOT_SUITABLE;
                }

                break;
            case "SaltwaterFish":
                fish = new SaltwaterFish(fishName, fishSpecies, price);

                if (aquarium instanceof SaltwaterAquarium) {
                    aquarium.addFish(fish);
                } else {
                    return ConstantMessages.WATER_NOT_SUITABLE;
                }
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_FISH_TYPE);
        }

        /*
                try {
            Aquarium aquarium = aquariums.get(aquariumName);
            aquarium.addFish(fish);
        } catch (IllegalStateException ex) {
            return ex.getMessage();
        }
         */

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_FISH_IN_AQUARIUM, fishType, aquariumName);
    }

    @Override
    public String feedFish(String aquariumName) {
        Aquarium aquarium = getAquarium(aquariumName);

        aquarium.feed();

        return String.format(ConstantMessages.FISH_FED, aquarium.getFish().size());
    }

    @Override
    public String calculateValue(String aquariumName) {

        Aquarium aquarium = getAquarium(aquariumName);

        Collection<Decoration> decorations = aquarium.getDecorations();
        Collection<Fish> fish = aquarium.getFish();

        double value = decorations.stream().mapToDouble(Decoration::getPrice).sum() +
                fish.stream().mapToDouble(Fish::getPrice).sum();

        return String.format(ConstantMessages.VALUE_AQUARIUM, aquariumName, value);
    }

    @Override
    public String report() {

        return aquariums.stream().map(Aquarium::getInfo).collect(Collectors.joining(System.lineSeparator()));


//        StringBuilder builder = new StringBuilder();
//
//        for (Aquarium aquarium : aquariums) {
//            builder.append(aquarium.getInfo()).append(System.lineSeparator());
//        }
//
//        return builder.toString();
    }


    private Aquarium getAquarium(String aquariumName) {
        Aquarium aquarium = null;

        for (Aquarium a : aquariums) {
            if (a.getName().equals(aquariumName)) {

                aquarium = a;
                break;
            }
        }
        return aquarium;
    }
}
