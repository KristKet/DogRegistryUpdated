

public class Dog {

    private Owner owner;
    private final String name;
    private final String breed;
    private int age;
    private final int weight;
    
    
    public Dog(String name, String breed, int age, int weight){
        this.age = age;
        this.breed = breed;
        this.weight = weight;
        this.name = name;
    }
    
    public double getTailLength(){
        if ( breed.toLowerCase().equals("tax") || breed.toLowerCase().equals("dachshund")){
            return 3.7;
        } else {
            return (age * weight) / 10.0;
        }
    }
    
    public void increaseAge (){
        age++;
    }
    
    public String getBreed () {
        return breed;
    }
    
    public int getAge () {
        return age;
    }
    
    public int getWeight () {
        return weight;
    }
    
    public String getName () {
        return name;
    }
    
    public void setOwner (Owner owner){
        this.owner = owner;
    }
    
    public Owner getOwner (){
        return owner;
    }
    @Override
    public String toString(){
        return String.format("%s", getName ());
    }
}