package ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import java.util.Locale;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

import model.*;


class GameFrame extends JFrame {
    private Deck deck = new Deck();
    private Player player;
    private Dealer dealer = new Dealer();

    // Componentes GUI 
    private JLabel lblPlayerBalance = new JLabel("Saldo: R$ 0.00");
    private JPanel dealerCardsPanel = new JPanel(new FlowLayout());//imagens
    private JPanel playerCardsPanel = new JPanel(new FlowLayout());//imagens
    //private JLabel lblPlayerHand = new JLabel();
    //private JLabel lblDealerHand = new JLabel();
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

    //interface
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

        // estado inicial
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

        // inicializar timer do dealer (used para animação)
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

        // perguntar nome e saldo inicial do player
        SwingUtilities.invokeLater(this::askPlayerInfo);
    }

    // imagem das cartas
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

    //carregar imagens
    private JLabel loadCardImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(170, 250, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    }


    // funcao para perguntar nome e saldo inicial do player 
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

    // funcao iniciar rodada
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
        } catch (ClassCastException ex) {
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
        btnAllIn.setEnabled(false);
        btnSurrender.setEnabled(true);
        btnStartRound.setEnabled(false);

        lblStatusMessage.setText("Rodada iniciada. Boa sorte!");
        updateLabels();

        // checar blackjac inicial (pagamento 1:1 )
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
    
    // funcao puxar carta
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

    // funcao stand / parar
    private void stand() {
        if (!roundActive) return;
        surrenderAvailable = false;
        btnSurrender.setEnabled(false);
        // iniciar comportamento do dealer com delay (animação)
        prepareDealerPlaysAndAnimate();
    }

    // funcao double down (dobrar aposta, puxar uma carta e parar) : pagamento 1:1
    private void doubleDown() {
        if (!roundActive) return;
        if (doubledDown) { lblStatusMessage.setText("Você já deu double."); return; }
        if (player.getBalance() < currentBet) { lblStatusMessage.setText("Saldo insuficiente para dobrar a aposta."); return; }
        // dobrar aposta: subtrair mesma quantidade e marcar como dobrado
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

    //funcao all in, aposta maxima
    private void allIn() {
        // só pode definir All-In quando não há rodada ativa
        if (roundActive) {
            lblStatusMessage.setText("All-In só pode ser selecionado antes de iniciar a rodada.");
            return;
        }

        double bal = player.getBalance();
        if (bal <= 0) {
            lblStatusMessage.setText("Saldo insuficiente para All-In.");
            return;
        }

        // define o spinner para o valor do saldo (All-In)
        spinnerBet.setValue(bal);

        // atualiza mensagem para o usuário
        lblStatusMessage.setText(String.format("All-In selecionado: R$ %.2f", bal));
    }

    // funcao desistir da mao inicial recebe metade da aposta
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

    // funcao jogada do dealer (soft 17)
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

    //verificar se ganhou double down
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

    //ver se ganhou rodada
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

    //resetar UI apos fim de rodada
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

    //verificar se o saldo acabou
    private void checkBankruptcy() {
        if (player.getBalance() <= 0) {
            lblStatusMessage.setText("Saldo zerado. Fim do jogo. Salve seus resultados.");
            btnStartRound.setEnabled(false);
            btnAllIn.setEnabled(false);
        }
    }

    // salvar informacoe do player em arquivo de texto
    private void saveAndExit() {
        String name = player.getName();
        double totalInvested = startingMoney;
        double profit = player.getProfit(startingMoney);
        int wins = player.getWins();
        int losses = player.getLosses();
        int empates = player.getPushes();
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
        sb.append("Total de Vitorias: ").append(wins).append(System.lineSeparator());
        sb.append("Total de derrotas: ").append(losses).append(System.lineSeparator());
        sb.append("Total de empates: ").append(empates).append(System.lineSeparator());
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
