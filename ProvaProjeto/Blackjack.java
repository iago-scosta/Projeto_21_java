import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Blackjack {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameFrame().setVisible(true));
    }
}

// ------------------------ Configurações visuais/modificáveis ------------------------
// Aqui você altera cores, tamanhos e posições mais facilmente.
class UIConfig {
    public static final Color BACKGROUND_COLOR = new Color(34, 139, 34); // verde "felt"
    public static final Color PANEL_COLOR = new Color(60, 179, 113);
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int DEALER_ANIMATION_DELAY_MS = 600; // delay entre cartas do dealer
    public static final String CARD_IMAGE_FOLDER = "cards"; // pasta relativa ao diretório de execução
}

// ------------------------ Modelo de jogo (Card, Deck, Hand, Player, Dealer) ------------------------
class Card {
    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
    private final String rank; // "A", "2".."10","J","Q","K"
    private final Suit suit;

    public Card(String rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    //imagem da carta
    public String getImagePath() {
        return "cards/" + rank + "_" + suit.name() + ".png";
}


    public String getRank() { return rank; }
    public Suit getSuit() { return suit; }

    public int getValue() {
        if ("A".equals(rank)) return 11; // default 11, Hand ajusta conforme necessário
        if ("J".equals(rank) || "Q".equals(rank) || "K".equals(rank)) return 10;
        return Integer.parseInt(rank);
    }

    @Override
    public String toString() {
        return rank + " of " + suit.name();
    }

    // Nome de arquivo sugerido para imagens: <rank>_<suit>.png, ex: A_SPADES.png ou 10_HEARTS.png
    public String imageFileName() {
        return rank + "_" + suit.name() + ".png";
    }
}

class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final Random rnd = new Random();

    public Deck() { reset(); }

    public final void reset() {
        cards.clear();
        String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        for (Card.Suit s : Card.Suit.values()) {
            for (String r : ranks) cards.add(new Card(r, s));
        }
        shuffle();
    }

    public void shuffle() { Collections.shuffle(cards, rnd); }

    public Card draw() {
        if (cards.isEmpty()) reset();
        return cards.remove(cards.size()-1);
    }
}

class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void add(Card c) { cards.add(c); }
    public List<Card> getCards() { return cards; }
    public void clear() { cards.clear(); }

    // Retorna o melhor valor da mão sem estourar, tratando A como 1 ou 11.
    public int bestValue() {
        int total = 0;
        int aces = 0;
        for (Card c : cards) {
            if ("A".equals(c.getRank())) { aces++; total += 11; }
            else total += c.getValue();
        }
        while (total > 21 && aces > 0) {
            total -= 10; // conta um Ás como 1 em vez de 11
            aces--;
        }
        return total;
    }

    public boolean isBlackjack() { // As + carta de valor 10 no inicio da rodaa
        if (cards.size() == 2) {
            boolean hasAce = cards.stream().anyMatch(c -> "A".equals(c.getRank()));
            boolean hasTen = cards.stream().anyMatch(c -> {
                String r = c.getRank();
                return "10".equals(r) || "J".equals(r) || "Q".equals(r) || "K".equals(r);
            });
            return hasAce && hasTen;
        }
        return false;
    }

    public boolean isBust() { return bestValue() > 21; } // perder

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card c : cards) sb.append(c.getRank()).append(' ');
        sb.append(" (" + bestValue() + ")");
        return sb.toString();
    }
}

class Player {
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

    public String getName() { return name; }
    public double getBalance() { return balance; }
    public void addBalance(double v) { balance += v; }
    public void subBalance(double v) { balance -= v; }
    public void setHand(Hand h) { hand = h; }
    public Hand getHand() { return hand; }

    public void recordBet(double amt) { totalBetAmount += amt; }
    public double getTotalBetAmount() { return totalBetAmount; }

    // atualizações de estatísticas
    public void recordWin() {
        totalRounds++; wins++; currentWinStreak++; currentLossStreak = 0;
        if (currentWinStreak > maxWinStreak) maxWinStreak = currentWinStreak;
    }
    public void recordLoss() {
        totalRounds++; losses++; currentLossStreak++; currentWinStreak = 0;
        if (currentLossStreak > maxLossStreak) maxLossStreak = currentLossStreak;
    }
    public void recordPush() { totalRounds++; pushes++; currentWinStreak = 0; currentLossStreak = 0; }

    public int getTotalRounds() { return totalRounds; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getPushes() { return pushes; }
    public int getMaxWinStreak() { return maxWinStreak; }
    public int getMaxLossStreak() { return maxLossStreak; }

    public double getProfit(double startingMoney) { return balance - startingMoney; }
}

class Dealer {
    private Hand hand = new Hand();

    public Hand getHand() { return hand; }
    public void setHand(Hand h) { hand = h; }
}

// ------------------------ GUI e lógica do jogo ------------------------
class GameFrame extends JFrame {
    private Deck deck = new Deck();
    private Player player;
    private Dealer dealer = new Dealer();

    // Componentes GUI (nomes mais descritivos)
    private JLabel lblPlayerBalance = new JLabel("Saldo: R$ 0.00");
    private JPanel dealerCardsPanel = new JPanel(new FlowLayout());//imagens
    private JPanel playerCardsPanel = new JPanel(new FlowLayout());//imagens
    private JLabel lblPlayerHand = new JLabel();
    private JLabel lblDealerHand = new JLabel();
    private JLabel lblStatusMessage = new JLabel("Bem-vindo ao Blackjack!");
    private JSpinner spinnerBet;
    private JButton btnStartRound = new JButton("Iniciar Rodada");
    private JButton btnHit = new JButton("Puxar (Hit)");
    private JButton btnStand = new JButton("Parar (Stand)");
    private JButton btnDouble = new JButton("Double Down");
    private JButton btnAllIn = new JButton("All-In");
    private JButton btnSurrender = new JButton("Surrender");
    private JButton btnSaveExit = new JButton("Salvar e Sair");

    private double startingMoney = 0.0;
    private double currentBet = 0.0;
    private boolean roundActive = false;
    private boolean doubledDown = false;
    private boolean surrenderAvailable = false; // surrender só no início da rodada

    // para animação do dealer
    private Timer dealerTimer;
    private final List<Card> dealerPendingDraws = new ArrayList<>();

    public GameFrame() {
        setTitle("Blackjack");
        setSize(UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(UIConfig.BACKGROUND_COLOR);

        // Top panel: player info
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UIConfig.PANEL_COLOR);
        topPanel.add(new JLabel("Jogador:"));
        topPanel.add(lblPlayerBalance);
        add(topPanel, BorderLayout.NORTH);

        // Center: hands
        JPanel center = new JPanel(new GridLayout(2,1));
        center.setOpaque(false);

        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        dealerPanel.setOpaque(false);
        dealerPanel.add(dealerCardsPanel, BorderLayout.CENTER);//imagem
        center.add(dealerPanel);

        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Jogador"));
        playerPanel.setOpaque(false);
        playerPanel.add(playerCardsPanel, BorderLayout.CENTER); //imagem
        center.add(playerPanel);

        add(center, BorderLayout.CENTER);


        // Bottom: controls
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(UIConfig.PANEL_COLOR);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setOpaque(false);
        controls.add(new JLabel("Aposta:"));
        spinnerBet = new JSpinner(new SpinnerNumberModel(10.0, 1.0, 1000000.0, 1.0));
        controls.add(spinnerBet);
        controls.add(btnStartRound);
        controls.add(btnHit);
        controls.add(btnStand);
        controls.add(btnDouble);
        controls.add(btnAllIn);
        controls.add(btnSurrender);
        controls.add(btnSaveExit);
        bottom.add(controls);

        JPanel msgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        msgPanel.setOpaque(false);
        msgPanel.add(lblStatusMessage);
        bottom.add(msgPanel);

        add(bottom, BorderLayout.SOUTH);

        // initial state
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        btnDouble.setEnabled(false);
        btnAllIn.setEnabled(false);
        btnSurrender.setEnabled(false);

        // listeners
        btnStartRound.addActionListener(e -> startRound());
        btnHit.addActionListener(e -> hit());
        btnStand.addActionListener(e -> stand());
        btnDouble.addActionListener(e -> doubleDown());
        btnAllIn.addActionListener(e -> allIn());
        btnSurrender.addActionListener(e -> surrender());
        btnSaveExit.addActionListener(e -> saveAndExit());

        // Initialize dealer timer (used para animação)
        dealerTimer = new Timer(UIConfig.DEALER_ANIMATION_DELAY_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!dealerPendingDraws.isEmpty()) {
                    Card c = dealerPendingDraws.remove(0);
                    dealer.getHand().add(c);
                    updateLabels();
                } else {
                    dealerTimer.stop();
                    // depois de terminar as compras do dealer, resolvemos a rodada
                    if (doubledDown) resolveRoundDoubleDown(); else resolveRound();
                }
            }
        });

        // ask player for name and starting money
        SwingUtilities.invokeLater(this::askPlayerInfo);
    }


    private void updateCardImages() {
        // limpa paineis
        dealerCardsPanel.removeAll();
        playerCardsPanel.removeAll();

        // cartas do dealer
        for (Card c : dealer.getHand().getCards()) {
            dealerCardsPanel.add(loadCardImage(c.getImagePath()));
        }

        // cartas do jogador
        for (Card c : player.getHand().getCards()) {
            playerCardsPanel.add(loadCardImage(c.getImagePath()));
        }

            dealerCardsPanel.revalidate();
            dealerCardsPanel.repaint();
            playerCardsPanel.revalidate();
            playerCardsPanel.repaint();
    }

    //imagens
    private JLabel loadCardImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(170, 250, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    }


    private void askPlayerInfo() {
        String name = JOptionPane.showInputDialog(this, "Digite seu nome:", "Nome", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) name = "Jogador";
        double money = 0.0;
        while (true) {
            String s = JOptionPane.showInputDialog(this, "Quantidade de dinheiro para inserir (ex: 1000):", "Dinheiro Inicial", JOptionPane.PLAIN_MESSAGE);
            if (s == null) { System.exit(0); }
            try {
                money = Double.parseDouble(s);
                if (money <= 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Informe um número positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        player = new Player(name, money);
        startingMoney = money;
        updateLabels();
        lblStatusMessage.setText("Bem-vindo, " + player.getName() + "! Faça sua aposta e clique em Iniciar Rodada.");
        // enable all-in always at menu
        btnAllIn.setEnabled(true);
    }

    private void updateLabels() {
        // Atualiza saldo e mensagens normalmente
        lblPlayerBalance.setText(String.format("%s - Saldo: R$ %.2f", player.getName(), player.getBalance()));

        // Atualiza as imagens das cartas
        updateCardImages();
    }


    private void startRound() {
        if (player.getBalance() <= 0) {
            lblStatusMessage.setText("Saldo zerado. Salve e saia ou reabra o jogo.");
            return;
        }
        if (roundActive) { lblStatusMessage.setText("Uma rodada já está ativa."); return; }

        double bet;
        // garantia adicional: o usuário pode digitar um valor no spinner (incluindo negativo)
        try {
            bet = ((Number)spinnerBet.getValue()).doubleValue();
        } catch (Exception ex) {
            lblStatusMessage.setText("Aposta inválida.");
            return;
        }

        if (bet <= 0) { lblStatusMessage.setText("Aposta deve ser maior que zero."); return; }
        if (bet > player.getBalance()) { lblStatusMessage.setText("Aposta maior que saldo disponível."); return; }

        // initialize
        currentBet = bet;
        player.recordBet(bet);
        player.subBalance(bet); // bloquear a aposta
        doubledDown = false;
        surrenderAvailable = true; // somente no começo da rodada

        Hand playerHand = new Hand();
        Hand dealerHand = new Hand();
        playerHand.add(deck.draw());
        playerHand.add(deck.draw());
        dealerHand.add(deck.draw());
        dealerHand.add(deck.draw());
        player.setHand(playerHand);
        dealer.setHand(dealerHand);

        roundActive = true;
        btnHit.setEnabled(true);
        btnStand.setEnabled(true);
        btnDouble.setEnabled(true);
        btnAllIn.setEnabled(true);
        btnSurrender.setEnabled(true);
        btnStartRound.setEnabled(false);

        lblStatusMessage.setText("Rodada iniciada. Boa sorte!");
        updateLabels();

        // check initial blackjack (pagamento 1:1 conforme solicitado)
        if (playerHand.isBlackjack()) {
            double totalReceived = currentBet * 2.0; // 1:1 -> recebe aposta + mesmo valor = dobro
            player.addBalance(totalReceived);
            player.recordWin();
            roundActive = false;
            btnHit.setEnabled(false);
            btnStand.setEnabled(false);
            btnDouble.setEnabled(false);
            btnAllIn.setEnabled(false);
            btnSurrender.setEnabled(false);
            btnStartRound.setEnabled(true);
            lblStatusMessage.setText("Blackjack! Você ganhou automaticamente (pagamento 1:1).");
            updateLabels();
            checkBankruptcy();
        }
    }

    private void hit() {
        if (!roundActive) return;
        // depois da primeira jogada, surrender não está mais disponível
        surrenderAvailable = false;
        btnSurrender.setEnabled(false);

        player.getHand().add(deck.draw());
        lblStatusMessage.setText("Você puxou uma carta.");
        updateLabels();
        if (player.getHand().isBust()) {
            player.recordLoss();
            roundActive = false;
            btnHit.setEnabled(false);
            btnStand.setEnabled(false);
            btnDouble.setEnabled(false);
            btnAllIn.setEnabled(false);
            btnSurrender.setEnabled(false);
            btnStartRound.setEnabled(true);
            lblStatusMessage.setText("Estourou! Você perdeu a aposta de R$ " + String.format("%.2f", currentBet));
            updateLabels();
            checkBankruptcy();
        }
    }

    private void stand() {
        if (!roundActive) return;
        surrenderAvailable = false;
        btnSurrender.setEnabled(false);
        // iniciar comportamento do dealer com delay (animação)
        prepareDealerPlaysAndAnimate();
    }

    private void doubleDown() {
        if (!roundActive) return;
        if (doubledDown) { lblStatusMessage.setText("Você já deu double."); return; }
        if (player.getBalance() < currentBet) { lblStatusMessage.setText("Saldo insuficiente para dobrar a aposta."); return; }
        // double the bet: deduct same amount and mark doubled
        player.subBalance(currentBet);
        double originalBet = currentBet;
        currentBet *= 2.0; // agora currentBet é o valor total arriscado
        player.recordBet(originalBet); // registrar a parte extra
        doubledDown = true;
        surrenderAvailable = false;
        btnSurrender.setEnabled(false);

        // player toma exatamente uma carta e então para
        player.getHand().add(deck.draw());
        lblStatusMessage.setText("Você deu Double Down e puxou exatamente uma carta.");
        updateLabels();

        if (player.getHand().isBust()) {
            player.recordLoss();
            roundActive = false;
            btnHit.setEnabled(false);
            btnStand.setEnabled(false);
            btnDouble.setEnabled(false);
            btnAllIn.setEnabled(false);
            btnSurrender.setEnabled(false);
            btnStartRound.setEnabled(true);
            lblStatusMessage.setText("Estourou após Double Down! Você perdeu a aposta de R$ " + String.format("%.2f", currentBet));
            updateLabels();
            checkBankruptcy();
            return;
        }

        // dealer joga com animação
        prepareDealerPlaysAndAnimate();
    }

    private void allIn() {
        if (roundActive) {
            lblStatusMessage.setText("All-In só pode ser selecionado antes de iniciar a rodada.");
            return;
        }
        double bal = player.getBalance();
        if (bal <= 0) { lblStatusMessage.setText("Saldo insuficiente para All-In."); return; }
        spinnerBet.setValue(bal);
        lblStatusMessage.setText("Aposta definida como All-In: R$ " + String.format("%.2f", bal));
    }

    private void surrender() {
        if (!roundActive || !surrenderAvailable) {
            lblStatusMessage.setText("Surrender só pode ser usado no começo da rodada, antes de qualquer ação.");
            return;
        }
        // retorna metade da aposta ao jogador e conta como perda
        double refund = currentBet / 2.0;
        player.addBalance(refund);
        player.recordLoss();
        roundActive = false;
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        btnDouble.setEnabled(false);
        btnAllIn.setEnabled(false);
        btnSurrender.setEnabled(false);
        btnStartRound.setEnabled(true);
        lblStatusMessage.setText("Você fez Surrender. Recebeu metade da aposta: R$ " + String.format("%.2f", refund));
        updateLabels();
        checkBankruptcy();
    }

    private void prepareDealerPlaysAndAnimate() {
        // Preenche a lista de cartas que o dealer deverá puxar seguindo a regra:
        // dealer deve comprar enquanto bestValue() < 17 (stand em 17 inclusive)
        dealerPendingDraws.clear();
        // clonamos estado atual do dealer para calcular quantas cartas ele precisará —
        // mas para simplicidade, apenas continuamos a desenhar até que a mão real atinja >=17
        // e usamos o timer para animar cada draw.
        // Para evitar desenhar tudo de uma vez, o timer desenha uma carta por intervalo.

        // desativar botões enquanto o dealer joga
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        btnDouble.setEnabled(false);
        btnAllIn.setEnabled(false);
        btnSurrender.setEnabled(false);

        // Preenche as cartas necessárias, mas usando draw() gradualmente na timer
        // Como precisamos saber quantas cartas serão puxadas, fazemos draws agora para enfileirar
        // e guardar numa lista separada; se o deck resetar no meio, o comportamento é ok.
        while (dealer.getHand().bestValue() < 17) {
            dealerPendingDraws.add(deck.draw());
            // atualiza a "simulação" temporariamente para não ficar em loop infinito
            dealer.getHand().add(dealerPendingDraws.get(dealerPendingDraws.size()-1));
            // se eu adicionei carta para simular e a mão já passou de 21, pare
            if (dealer.getHand().bestValue() >= 17) break;
        }
        // Remover as cartas que acabamos de adicionar na simulação para restaurar mão real
        // (isto foi feito para decidir quantas cartas desenhar). Em seguida, vamos repopular
        // com draws reais pelo timer. Simulação simplificada:
        for (int i = 0; i < dealerPendingDraws.size(); i++) {
            // remover as últimas cartas adicionadas
            List<Card> list = dealer.getHand().getCards();
            if (!list.isEmpty()) list.remove(list.size()-1);
        }

        // Iniciar timer: mas se não houver cartas pendentes (ou dealer já >=17), apenas resolver
        if (dealerPendingDraws.isEmpty()) {
            if (doubledDown) resolveRoundDoubleDown(); else resolveRound();
        } else {
            // jogar a primeira carta imediatamente para dar sensação de começo,
            // o timer vai continuar desenhando as restantes (se houver)
            dealerTimer.start();
        }
    }

    private void resolveRoundDoubleDown() {
        // comparar mãos e aplicar regras de double down (pagamento 1:1 em caso de vitória)
        int p = player.getHand().bestValue();
        int d = dealer.getHand().bestValue();
        boolean playerBust = p > 21;
        boolean dealerBust = d > 21;

        if (playerBust) {
            player.recordLoss();
            lblStatusMessage.setText("Você estourou após Double Down. Perdeu R$ " + String.format("%.2f", currentBet));
        } else if (dealerBust) {
            double totalReceived = currentBet * 2.0; // 1:1 -> recebe stake + same amount
            player.addBalance(totalReceived);
            player.recordWin();
            lblStatusMessage.setText("Dealer estourou. Você venceu (Double Down). Recebeu R$ " + String.format("%.2f", totalReceived));
        } else {
            if (p > d) {
                double totalReceived = currentBet * 2.0;
                player.addBalance(totalReceived);
                player.recordWin();
                lblStatusMessage.setText("Você venceu a rodada (Double Down). Recebeu R$ " + String.format("%.2f", totalReceived));
            } else if (p == d) {
                player.addBalance(currentBet);
                player.recordPush();
                lblStatusMessage.setText("Empate (push) após Double Down. Aposta retornada.");
            } else {
                player.recordLoss();
                lblStatusMessage.setText("Você perdeu a rodada após Double Down.");
            }
        }
        endRoundCleanup();
    }

    private void resolveRound() {
        int p = player.getHand().bestValue();
        int d = dealer.getHand().bestValue();
        boolean playerBust = p > 21;
        boolean dealerBust = d > 21;

        if (playerBust) {
            player.recordLoss();
            lblStatusMessage.setText("Você estourou. Perdeu R$ " + String.format("%.2f", currentBet));
        } else if (dealerBust) {
            double totalReceived = currentBet * 1.5; // pagamento 3:2 -> total retornado = bet * 1.5
            player.addBalance(totalReceived);
            player.recordWin();
            lblStatusMessage.setText("Dealer estourou. Você venceu! Recebeu R$ " + String.format("%.2f", totalReceived));
        } else {
            if (p > d) {
                double totalReceived = currentBet * 1.5; // pagamento 3:2
                player.addBalance(totalReceived);
                player.recordWin();
                lblStatusMessage.setText("Você venceu a rodada! Recebeu R$ " + String.format("%.2f", totalReceived));
            } else if (p == d) {
                player.addBalance(currentBet);
                player.recordPush();
                lblStatusMessage.setText("Empate (push). Aposta retornada.");
            } else {
                player.recordLoss();
                lblStatusMessage.setText("Você perdeu a rodada. Perdeu R$ " + String.format("%.2f", currentBet));
            }
        }
        endRoundCleanup();
    }

    private void endRoundCleanup() {
        roundActive = false;
        doubledDown = false;
        surrenderAvailable = false;
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        btnDouble.setEnabled(false);
        btnAllIn.setEnabled(true);
        btnSurrender.setEnabled(false);
        btnStartRound.setEnabled(true);
        btnAllIn.setEnabled(true);
        updateLabels();
        checkBankruptcy();
    }

    private void checkBankruptcy() {
        if (player.getBalance() <= 0) {
            lblStatusMessage.setText("Saldo zerado. Fim do jogo. Salve seus resultados.");
            btnStartRound.setEnabled(false);
            btnAllIn.setEnabled(false);
        }
    }

    private void saveAndExit() {
        String name = player.getName();
        double totalInvested = startingMoney;
        double profit = player.getProfit(startingMoney);
        int maxW = player.getMaxWinStreak();
        int maxL = player.getMaxLossStreak();
        int rounds = player.getTotalRounds();
        double winPct = rounds == 0 ? 0.0 : (player.getWins() * 100.0 / rounds);

        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(name).append(System.lineSeparator());
        sb.append(String.format(Locale.US, "Total investido: R$ %.2f", totalInvested)).append(System.lineSeparator());
        sb.append(String.format(Locale.US, "Ganho/Prejuizo: R$ %.2f", profit)).append(System.lineSeparator());
        sb.append("Maior sequencia de vitorias: ").append(maxW).append(System.lineSeparator());
        sb.append("Maior sequencia de derrotas: ").append(maxL).append(System.lineSeparator());
        sb.append("Total de rodadas: ").append(rounds).append(System.lineSeparator());
        sb.append(String.format(Locale.US, "Porcentagem de vitorias: %.2f%%", winPct)).append(System.lineSeparator());

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(name + "_blackjack_result.txt"));
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write(sb.toString());
                JOptionPane.showMessageDialog(this, "Resultados salvos em: " + f.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        System.exit(0);
    }
}

/*
 * README / notas rápidas (no chat também explicarei):
 *
 * 1) Lógica do dealer:
 *    - O dealer compra enquanto bestValue() < 17 e para em 17 ou mais (inclui soft 17 como "stand").
 *    - A animação usa javax.swing.Timer com UIConfig.DEALER_ANIMATION_DELAY_MS milissegundos entre cartas.
 *
 * 2) Onde alterar cores/posições:
 *    - No topo do arquivo existe a classe UIConfig (BACKGROUND_COLOR, PANEL_COLOR, WINDOW_*).
 *    - Para reposicionar componentes, edite a construção dos painéis em GameFrame() (BorderLayout, GridLayout, etc.).
 *
 * 3) Como transformar em executável .jar:
 *    - Compile: javac Blackjack.java
 *    - Crie o JAR com um manifesto indicando a classe main:
 *        echo "Main-Class: Blackjack" > manifest.txt
 *        jar cfm Blackjack.jar manifest.txt *.class
 *    - Rode com: java -jar Blackjack.jar
 *    - Se usar imagens (pasta cards/), inclua-as no JAR mantendo a estrutura: jar cfm Blackjack.jar manifest.txt *.class cards/*
 *
 * 4) Como adicionar imagens para cartas:
 *    - Coloque as imagens na pasta "cards" ao lado do .class/.jar.
 *    - Nome sugerido: <rank>_<SUIT>.png (ex: A_SPADES.png, 10_HEARTS.png, J_DIAMONDS.png).
 *    - No código você pode criar ImageIcon ic = new ImageIcon("cards/" + card.imageFileName());
 *      e desenhar nos JLabels (escalando quando necessário). Para boas práticas dentro de JAR, use getResourceAsStream.
 *
 */
