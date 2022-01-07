import java.util.*;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    
    private final Scanner scanner = new Scanner(System.in);
    private final Register register = new Register();
    private final ArrayList<String> menuOptions = new ArrayList<>();
    private boolean running = true;
    
    
    public static void main(String[] args) {
        new Main ().run();
    }
    
    private void run() {
        menuAdd ();
        runCommandLoop();
        scanner.close ();
    }
    
    private String menuPrint() {
        StringJoiner joiner = new StringJoiner("\n");
        for ( String s : menuOptions ) {
            joiner.add(s);
        }
        return joiner.toString();
    }
    
    private void menuAdd() {
        menuOptions.add("Menyalternativ");
        menuOptions.add("1  \u2014 register new dog");
        menuOptions.add("3  \u2014 increase age");
        menuOptions.add("4  \u2014 list dogs");
        menuOptions.add("5  \u2014 remove dog");
        menuOptions.add("6  \u2014 register new user");
        menuOptions.add("7  \u2014 list users");
        menuOptions.add("8  \u2014 remove user");
        menuOptions.add("9  \u2014 start auction");
        menuOptions.add("10 \u2014 list auctions");
        menuOptions.add("11 \u2014 list bids");
        menuOptions.add("11 \u2014 make bid");
        menuOptions.add("11 \u2014 close auction");
        menuOptions.add("12 \u2014 give dog");
        menuOptions.add("13 \u2014 exit");
    }
    private void runCommandLoop() {
        while ( running ) {
            System.out.println("Ange kommando! ?> ");
            String input = scanner.nextLine();
            input = input.toLowerCase();
            handleCommand(input);
        }
    }
    
    private String readString(String description) {
        String tempString;
        boolean correctInput = false;
        do {
            System.out.print(description);
            tempString = scanner.nextLine().trim();
            if ( tempString.isEmpty() ) {
                System.out.println("Error : kan inte vara tom.");
            } else {
                correctInput = true;
            }
        } while ( !correctInput );
        
        return standardizeText(tempString);
    }
    
    private String standardizeText(String s) {
        return s.trim().substring(0, 1).toUpperCase() + s.trim().substring(1).toLowerCase();
    }
    
    private void handleCommand(String input) {
        switch ( input ) {
            case "register new dog": {
                registerNewDog();
                break;
            }
            case "increase age": {
                increaseAge();
                break;
            }
            case "list dogs": {
                listDogs();
                break;
            }
            case "remove dog": {
                removeDog();
                break;
            }
            case "register new user": {
                registerNewUser();
                break;
            }
            case "list users": {
                listUsers();
                break;
            }
            case "remove user": {
                removeUser();
                break;
            }
            case "start auction": {
                startAuction();
                break;
            }
            case "list auctions": {
                listAuctions();
                break;
            }
            case "close auction": {
                closeAuction();
                break;
            }
            case "make bid": {
                makeBid();
                break;
            }
            case "list bids": {
                listBids();
                break;
            }
            case "give dog": {
                giveDog();
                break;
            }
            case "exit": {
                running = false;
                break;
            }
            case "help": {
                System.out.println (menuPrint ());
                break;
            }
            default: {
                System.out.println("Error. Skriv 'help' för tillgängliga alternativ ?>");
            }
        }
    }
    
    private void giveDog () {
        String dogGiven = readString ( "Vilken hund ska ges bort?>" );
        Dog dog = register.getDogByName(dogGiven);
        if ( dog != null && dog.getOwner () == null ) {
            String faHund = readString ( "vem ska få en hund?>" );
            Owner owner = register.getUserByName ( faHund );
            if ( owner != null ) {
                dog.setOwner ( owner );
                owner.expandingArrayForDogs ( dog );
                System.out.printf("%s äger nu %s", owner.getName (), dog.getName ());
                System.out.println ();
            }else System.out.println ("Error: Ingen med det namnet");
        } else if ( dog == null ) {
            System.out.println("Error: Ingen hund med det namnet");
        } else {
            System.out.println("Error: Hund har redan ägare");
        }
    }
    
    private void closeAuction() {
        String dogName = readString ("Vad är hundens namn?>");
        Auction auction = register.getAuctionByDog (dogName);
        if ( auction != null && auction.hasBids() ) {
            Dog dogInAuction = auction.getDog();
            Owner winner = auction.getHighestBid().getBidder ();
            winner.expandingArrayForDogs ( dogInAuction );
            dogInAuction.setOwner (winner);
            System.out.printf("Auktionen avslutad. Vinnande bud %dkr gjort av %s\n", auction.getHighestBid().getBidSum (), winner.getName ());
            register.unregisterAuction(auction);
        } else if ( auction == null ) {
            System.out.println("Error");
        } else if ( !auction.hasBids() ) {
            Dog dogInAuction = auction.getDog();
            System.out.println("Auktion avslutad " + dogInAuction.getName ());
            register.unregisterAuction(auction);
        } else {
            System.out.println("Error");
        }
    }
    
    private void makeBid() {
        String userName = readString ("Vad är ägarens namn?>");
        Owner user = register.getUserByName(userName);
        if ( user != null ) {
            String dogName = readString ("Vad är hundens namn?>");
            Auction auction = register.getAuctionByDog (dogName);
            if ( auction != null ) {
                boolean bidTooLow = true;
                while ( bidTooLow ) {
                    System.out.printf("minsta summa (min %d) ?>", auction.hasBids() ? auction.getHighestBid().getBidSum () + 1 : 1);
                    int bidAmount = scanner.nextInt();
                    scanner.nextLine();
                    if ( bidAmount > (auction.hasBids() ? auction.getHighestBid().getBidSum () : 0) ) {
                        Bid bid = new Bid(user, bidAmount);
                        auction.addBid(bid);
                        bidTooLow = false;
                    } else {
                        System.out.println("Error: Too low bid!");
                    }
                }
            } else {
                System.out.println("Error: Ej till salu");
            }
        } else {
            System.out.println("Error: Ingen med det namnet");
        }
    }
    
    private void startAuction() {
        String dogName = readString("Vad är hundens namn?>");
        Dog dog = register.getDogByName(dogName);
        
        if ( dog != null && !register.dogIsInAuction(dog) && dog.getOwner () == null ) {
            Auction auction = new Auction(dog);
            register.registerAuction(auction);
            System.out.printf("%s kommer nu att auktioneras i auktion #%d\n", dog.getName (), auction.getAuctionsID ());
        } else if ( dog == null ) {
            System.out.println("Error: Ingen hund med det namnet");
        } else if ( dog.getOwner () != null ) {
            System.out.println("Error: Hund har redan ägare");
        } else {
            System.out.println("Error: Hund redan till salu");
        }
    }
    
    private void registerNewUser() {
        String name = readString ("Vad är ägarens namn ?>");
        register.registerUser(new Owner (name));
        System.out.printf("%s är nu tillagd\n", name);
    }
    
    private void registerNewDog() {
        String name = readString ("Vad heter hunden?>");
        String breed = readString ("Vad är det för hundras?>");
        System.out.println("Hur gammal är hunden?>");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Hur mycket väger hunden?>");
        int weight = scanner.nextInt();
        scanner.nextLine();
        register.registerDog(new Dog(name, breed, age, weight));
        System.out.println("Hund registrerad");
    }
    
    private void increaseAge() {
        String name = readString ("Vilken hund vill du öka ålder på?>");
        Dog dog = register.getDogByName(name);
        if ( dog != null ) {
            dog.increaseAge ();
            System.out.printf("Hund %s är nu äldre", dog.getName ());
        } else {
            System.out.println("Error : hund med det namnet fanns ej i registret");
        }
    }
    
    private void listBids() {
        String name = readString ("Vilken hund söker du budinfo om?>");
        Auction auction = register.getAuctionByDog (name);
        if ( auction != null && auction.hasBids() ) {
            auction.sendBid ( auction );
        } else if ( auction == null ) {
            System.out.println("Error: Hund ej till salu");
        } else if ( auction.lengthOfBidArray () == 0 ) {
            System.out.println("Error: Inga bud registrerade");
        } else {
            System.out.println("inga bud: listning av bud");
        }
        
    }
    
    private void listAuctions() {
        register.printAuctionList ();
    }
    
    private void listDogs() {
        if ( !register.isThereRegisteredDogs () ) {
            System.out.println("Vilken är kortaste svanslängden?>");
            double tailLength = scanner.nextDouble();
            scanner.nextLine();
            register.printDogList ( tailLength );
        } else {
            System.out.println("Error: inga hundar registrerade");
        }
        
    }
    
    private void listUsers() {
        register.printUserList ();
    }
    
    private void removeDog() {
        String name = readString ("Vilken hund vill du ta bort?>");
        Dog dog = register.getDogByName(name);
        if ( dog != null && dog.getOwner () != null ){
            dog.getOwner ().findAndRemoveDog ( name );
        }
        if ( register.dogIsInAuction ( dog ) ) {
            Auction auction = register.getAuctionByDog ( name );
            Dog dogInAuction = auction.getDog();
            System.out.println("Auktion avslutad " + dogInAuction.getName ());
            register.unregisterAuction(auction);
            register.unregisterDog ( dog );
        }
        register.unregisterDog ( dog );
    }
    
    private void removeUser() {
        String name = readString ("Vilken ägare vill du ta bort?>");
        Owner user = register.getUserByName(name);
        register.unregisterUser(user);
    }
}
