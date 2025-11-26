package model;

public class Player {
    private final String name;
    private double balance;
    private Hand hand = new Hand();

    // estatísticas
    private int totalRounds = 0;
    private int wins = 0;
    private int losses = 0;
    private int pushes = 0;
    private int maxWinStreak = 0;
    private int maxLossStreak = 0;
    private int currentWinStreak = 0;
    private int currentLossStreak = 0;
    private double totalBetAmount = 0.0;

    public Player(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double v) {
        balance += v;
    }

    public void subBalance(double v) {
        balance -= v; 
    }

    public void setHand(Hand h) {
        hand = h;
    }

    public Hand getHand() {
        return hand;
    }

    public void recordBet(double amt) {
        totalBetAmount += amt;
    }

    public double getTotalBetAmount() {
        return totalBetAmount;
    }

    // atualizações de estatísticas
    //player ganhou
    public void recordWin() {
        totalRounds++; wins++; currentWinStreak++; currentLossStreak = 0;
        if (currentWinStreak > maxWinStreak) maxWinStreak = currentWinStreak;
    }
     
    //player perdeu
    public void recordLoss() {
        totalRounds++; losses++; currentLossStreak++; currentWinStreak = 0;
        if (currentLossStreak > maxLossStreak) maxLossStreak = currentLossStreak;
    }
    
    //player empatou
    public void recordPush() {
        totalRounds++; pushes++; currentWinStreak = 0; currentLossStreak = 0;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getPushes() {
        return pushes;
    }

    public int getMaxWinStreak() {
        return maxWinStreak;
    }

    public int getMaxLossStreak() {
        return maxLossStreak;
    }
    
    //funcao pra saber o lucro
    public double getProfit(double startingMoney) {
        return balance - startingMoney;
    }
}