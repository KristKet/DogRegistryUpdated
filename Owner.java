import java.util.ArrayList;
import java.util.Arrays;

public class Owner {
    
    private Dog[] ownedDogs = new Dog[0];
    private final String name;
    
    public Owner (String name){
        this.name = name;
    }
    
    public String getName () {
        return name;
    }
    
    public Dog[] getDogs(){
        return Arrays.copyOf ( ownedDogs, ownedDogs.length );
    }
    
    public void findAndRemoveDog (String dogName ) {
        for ( int index = 0; index < ownedDogs.length; index++ ) {
            if ( ownedDogs[ index ].getName ().equalsIgnoreCase ( dogName ) ) {
                ownedDogs[index] = null;
                ownedDogs =  handleNullArray ( ownedDogs );
            }
        }
    }
    private Dog[] handleNullArray (Dog[] a) {
        ArrayList<Dog> removedNull = new ArrayList<>();
        for (Dog o : a)
            if (o != null)
                removedNull.add(o);
        return removedNull.toArray(new Dog[0]);
    }
    
    public void expandingArrayForDogs (Dog dog) {
        Dog[] newArray = new Dog[ ownedDogs.length + 1 ];
        System.arraycopy ( ownedDogs, 0, newArray, 0, ownedDogs.length );
        ownedDogs = newArray;
        ownedDogs[ ownedDogs.length-1] = dog;
    }
    
}