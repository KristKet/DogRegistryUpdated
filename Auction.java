import java.util.Arrays;

@SuppressWarnings("SpellCheckingInspection")
public class Auction {
    
    private static int instanceCount;
    private final Dog dogForAuction;
    private final int auctionID;
    private Bid[] bids = new Bid[1];
    
    public Auction(Dog dog){
        instanceCount++;
        auctionID = instanceCount;
        dogForAuction = dog;
    }
    
    public void addBid(Bid bid){
        removeBidsFromUser(bid.getBidder ());
        bids[ bids.length-1] = bid;
        if ( bids[0] == null){
            bids[0] = bids[1];
        }
        bids = Arrays.copyOf( bids, bids.length+1);
    }
    
    public int getAuctionsID (){
        return auctionID;
    }
    
    public Dog getDog(){
        return dogForAuction;
    }
    public boolean checkIfTrue (Auction auction, Owner user) {
        sortBids ();
        for ( int i = 0; i < auction.lengthOfBidArray () - 1; i++ ) {
            Bid[] bids = auction.bids;
            if ( bids[ i ].getBidder ().equals ( user ) ) {
                return true;
            }
        }
        return false;
    }
    
    public void sendBid (Auction auction) {
        sortBids ();
        System.out.println("Här är aktuella buden");
        for ( int i = 0; i < auction.lengthOfBidArray () - 1; i++ ) {
            System.out.printf("%s %d kr\n", auction.bids[ i ].getBidder ().getName (), auction.bids[ i ].getBidSum ());
        }
    }
    public int lengthOfBidArray (){
        return bids.length;
    }
    public boolean hasBids(){
        return bids.length > 1;
    }
    
    public Bid getHighestBid(){
        sortBids();
        if (this.hasBids()){
            return bids[0];
        }
        return new Bid( new Owner ("PlaceHolder"), -1);
    }
    
    private void sortBids() {
        for ( int i = 1; i < bids.length - 1; i++) {
            int indexToSort = i;
            while (indexToSort > 0 && bids[indexToSort].getBidSum () > bids[indexToSort - 1].getBidSum ()) {
                Bid bid = bids[indexToSort];
                bids[indexToSort] = bids[indexToSort - 1];
                bids[indexToSort - 1] = bid;
                indexToSort--;
            }
        }
    }
    
    public String getThreeBiggestBids (){
        sortBids();
        StringBuilder sb = new StringBuilder ();
        for ( int i = 0; i< bids.length-1 && i < 3; i++){
            sb.append ( bids[ i ].getBidder ().getName () ).append ( " " ).append ( bids[ i ].getBidSum () ).append ( " kr" );
            if (i < bids.length -2){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    public void removeBidsFromUser(Owner user){
        boolean bidIsFound = false;
        int counter = 0;
        Bid[] temp = new Bid[ bids.length];
        for ( Bid bid : bids ) {
            if ( bid != null ) {
                if ( bid.getBidder ().equals ( user ) && !bidIsFound ) {
                    bidIsFound = true;
                } else {
                    temp[ counter ] = bid;
                    counter++;
                }
            }
        }
        if (bidIsFound) {
            bids = Arrays.copyOf(temp, temp.length - 1);
        }
    }
}