import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("SpellCheckingInspection")
public class Register {
    
    private final ArrayList<Dog> dogArrayList = new ArrayList<> ();
    private final ArrayList<Owner> ownerArrayList = new ArrayList<> ();
    private final ArrayList<Auction> auctionArrayList = new ArrayList<> ();
    
    public void registerAuction (Auction auction) {
        auctionArrayList.add ( auction );
    }
    
    public void registerDog (Dog dog) {
        dogArrayList.add ( dog );
    }
    
    public void registerUser (Owner user) {
        ownerArrayList.add ( user );
    }
    
    public boolean isThereRegisteredDogs () {
        return dogArrayList.isEmpty ();
    }
    
    public void printDogList (double d) {
        sortDogList ();
        for ( Dog dog : dogArrayList ) {
            if ( dog.getTailLength () >= d && dog.getOwner () == null ) {
                System.out.printf ( "%s (%s, %d år, %d kilo, %.1f cm svans) \n", dog.getName ().toLowerCase (), dog.getBreed (), dog.getAge (), dog.getWeight (), dog.getTailLength () );
            } else if ( dog.getTailLength () >= d && dog.getOwner () != null ) {
                System.out.printf ( "%s (%s, %d år, %d kilo, %.1f cm svans, Ägare %s) \n", dog.getName ().toLowerCase (), dog.getBreed (), dog.getAge (), dog.getWeight (), dog.getTailLength (), dog.getOwner ().getName () );
            }
        }
        System.out.println ( "uppgift utförd" );
    }
    
    public void printAuctionList () {
        if ( !auctionArrayList.isEmpty () ) {
            for ( Auction auction : auctionArrayList ) {
                System.out.printf ( "Auction #%d: %s. Top bids: [%s]\n", auction.getAuctionsID (), auction.getDog ().getName (), auction.getThreeBiggestBids () );
            }
        } else {
            System.out.println ( "Error: ingen pågående auktion" );
        }
    }
    
    public void printUserList () {
        if ( !ownerArrayList.isEmpty () ) {
            for ( Owner user : ownerArrayList ) {
                System.out.println ( user.getName () + Arrays.toString ( user.getDogs () ) );
            }
            System.out.println ( "Uppgift utförd" );
        } else {
            System.out.println ( "Error: inga registrerade namn" );
        }
    }
    
    public void unregisterDog (Dog dog) {
        if ( dog != null ) {
            dogArrayList.remove ( dog );
        } else {
            System.out.println ( "Error: avregistrering hund" );
        }
    }
    
    public void unregisterUser (Owner user) {
        if ( user != null ) {
            ownerArrayList.remove ( user );
            for ( Auction auction : getAuctionByUser ( user ) ) {
                auction.removeBidsFromUser ( user );
            }
            for ( Dog dog : user.getDogs () ) {
                unregisterDog(dog);
            }
            System.out.println ("Ägare borttagen");
        }else{
            System.out.println("Error: avregistrering ägare");
        }
    }
    
    public void unregisterAuction(Auction auction){
        if (auction != null){
            auctionArrayList.remove(auction);
        }else{
            System.out.println("Error: avregistrering auktion");
        }
    }
    
    public Auction getAuctionByDog (String name){
        if (!auctionArrayList.isEmpty()){
            for (Auction auction : auctionArrayList ){
                if (auction.getDog().getName ().equalsIgnoreCase (name)){
                    return auction;
                }
            }
        }
        return null;
    }
    
    private ArrayList<Auction> getAuctionByUser (Owner user){
        ArrayList<Auction> userAuctionList = new ArrayList<>();
        for (Auction auction : auctionArrayList ){
            if (auction.hasBids()){
                boolean ifTrue = auction.checkIfTrue ( auction, user );
                if ( ifTrue ) {
                    userAuctionList.add(auction);
                } else System.out.println ();
            }
        }
        return new ArrayList<>( userAuctionList );
    }
    
    public Dog getDogByName(String name){
        for (Dog dog : dogArrayList ){
            if (dog.getName ().equals(name)){
                return dog;
            }
        }
        return null;
    }
    
    public Owner getUserByName(String name){
        for ( Owner user : ownerArrayList ){
            if (user.getName ().equals(name)){
                return user;
            }
        }
        return null;
    }
    
    public boolean dogIsInAuction(Dog dog){
        for ( Auction auction : auctionArrayList ){
            if ( auction.getDog ().equals ( dog ) ){
                return true;
            }
        }
        return false;
    }
    
    //Insertion sort på båda, pos kommer spara värdet och gå igenom hela listan och söka efter
    //ett mindre värde, om det ej finns sätts pos som minsta värde
    private void sortDogList(){
        ArrayList<Dog> tempList = dogArrayList;
        for ( int i = 0; i < dogArrayList.size(); i++) {
            int pos = i;
            for ( int j = i; j < dogArrayList.size (); j++ ) {
                if ( dogArrayList.get ( j ).getTailLength () < tempList.get ( pos ).getTailLength () )
                    pos = j;
                if ( dogArrayList.get ( j ).getTailLength () == tempList.get ( pos ).getTailLength ()
                        && dogArrayList.get ( j ).getName ().compareTo ( dogArrayList.get ( pos ).getName () ) < 0 )
                    /*compareTo jämför unicode-värden,
                     * VERSALER har ett lägre värde och det måste hanteras därefter
                     */
                    pos = j;
            }
            Dog min = dogArrayList.get ( pos );
            dogArrayList.set ( pos, dogArrayList.get ( i ) );
            dogArrayList.set ( i, min );
        }
    }
}