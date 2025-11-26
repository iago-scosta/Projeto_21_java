package model;

public class Dealer {

    //instancia a mao do dealer
    private Hand hand = new Hand();

    public Hand getHand() {
        return hand; 
    }
    public void setHand(Hand h) {
        hand = h; 
    }
}