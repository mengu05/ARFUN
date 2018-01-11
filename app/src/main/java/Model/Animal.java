package Model;

/**
 * Created by mengu on 11/1/2018.
 */

public class Animal {
    private String Name;
    private String Species;
    private String Description;
    private String iconLink;
    private String imageLink;

    public Animal(String name, String species, String description, String iconLink, String imageLink) {

        Name = name;
        Species = species;
        Description = description;
        this.iconLink = iconLink;
        this.imageLink = imageLink;
    }

    public Animal() {

    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpecies() {
        return Species;
    }

    public void setSpecies(String species) {
        Species = species;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
