
public class Bid {
    
    private final Owner bidder;
    private final int bidSum;
    
    public Bid(Owner bidder, int bidSum){
        this.bidder = bidder;
        this.bidSum = bidSum;
    }
    
    public Owner getBidder (){
        return bidder;
    }
    
    public int getBidSum (){
        return bidSum;
    }
}
