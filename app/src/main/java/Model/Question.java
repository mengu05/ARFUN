package Model;

/**
 * Created by mengu on 10/1/2018.
 */

public class Question {
    private String imageLink;
    private String animalName;

    public Question() {
    }

    public Question(String imageLink, String animalName) {
        this.imageLink = imageLink;
        this.animalName = animalName;
    }

    public String getAnimalName() {
        return animalName;
    }
    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
    public String getImageLink() {
        return imageLink;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
