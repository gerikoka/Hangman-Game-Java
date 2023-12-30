/* Geri Koka
 * Personal Hangman Project
 * August 2023
 */

package HangmanGameProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;

public class Main extends JFrame {

    // Declare instance variables
    private JLabel wordLabel;
    private JButton[] letterButtons;
    private JButton[] arrayButtons;

    // Arrays of words for the game
    private String[] wordArray1 = {"LION", "TIGER", "ELEPHANT", "GIRAFFE", "HIPPOPOTAMUS", "RHINOCEROS", "CROCODILE", "KANGAROO", "PENGUIN", "GORILLA", "ZEBRA", "WOLF", "JAGUAR", "BISON", "LEOPARD", "COUGAR", "BUFFALO", "PANTHER", "CHEETAH", "GIRAFFE"};
    private String[] wordArray2 = {"PIZZA", "BURGER", "SUSHI", "STEAK", "TACOS", "PASTA", "SALAD", "CHICKEN", "SHRIMP", "LOBSTER", "PIE", "CAKE", "ICE CREAM", "DONUTS", "FRIES", "NACHOS", "CHIPS", "COOKIE", "BACON", "SANDWICH"};

    // Game-related variables
    private String secretWord;
    private char[] guessedWord;
    private int attemptsLeft = 6;
    private int incorrectGuesses = 0;

    // Panels for different sections of the game
    private JPanel welcomePanel;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel welcomeHangmanPanel;

    private boolean arraySelected = false;

    public Main() {
        // set up the frame
        setTitle("Hangman Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a JPanel for the layout
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(Color.ORANGE);

        JLabel welcomeLabel = new JLabel("HANGMAN", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        welcomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);

        welcomeHangmanPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWelcomeHangman((Graphics2D) g);
            }
        };
        welcomeHangmanPanel.setPreferredSize(new Dimension(400, 600));
        welcomePanel.add(welcomeHangmanPanel, BorderLayout.CENTER);


        JButton startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());
        welcomePanel.add(startButton, BorderLayout.SOUTH);


        // Setting the frame as visible
        add(welcomePanel);
        setVisible(true);
        startAnimation();

        // Creating and configuring the main game panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.ORANGE);

        wordLabel = new JLabel();
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        mainPanel.add(wordLabel, BorderLayout.NORTH);

        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.ORANGE);

        arrayButtons = new JButton[2];
        arrayButtons[0] = new JButton("Animals");
        arrayButtons[0].setPreferredSize(new Dimension(100, 100));
        arrayButtons[0].addActionListener(new ArraySelectionListener(wordArray1));

        arrayButtons[1] = new JButton("Foods");
        arrayButtons[1].setPreferredSize(new Dimension(100, 100));
        arrayButtons[1].addActionListener(new ArraySelectionListener(wordArray2));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 1; // Add this line

        buttonPanel.add(arrayButtons[0], constraints);

        constraints.gridx = 1;

        buttonPanel.add(arrayButtons[1], constraints);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Setting the frame as visible
        setVisible(true);
    }

    // Method to draw the Hangman figure during the welcome animation
    private void drawWelcomeHangman(Graphics2D g2d) {
        // Set the background color
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Define the body parts of the Hangman figure in the desired order
        Shape[] bodyParts = new Shape[6];
        bodyParts[0] = new Ellipse2D.Double(1130, 150, 100, 100);    // Head
        bodyParts[1] = new Line2D.Double(1180, 250, 1180, 400);      // Body
        bodyParts[2] = new Line2D.Double(1180, 400, 1120, 500);      // Left leg
        bodyParts[3] = new Line2D.Double(1180, 400, 1240, 500);      // Right leg
        bodyParts[4] = new Line2D.Double(1120, 280, 1180, 350);        // Left arm
        bodyParts[5] = new Line2D.Double(1180, 350, 1240, 280);      // Right arm

        // Draw the Hangman figure parts in the defined order
        g2d.setColor(Color.BLACK);
        long currentTime = System.currentTimeMillis();
        int partIndex = (int) ((currentTime / 1000) % bodyParts.length);
        for (int i = 0; i <= partIndex; i++) {
            g2d.setStroke(new BasicStroke(10.0f));
            g2d.draw(bodyParts[i]);
        }
    }

    // Method to start the welcome animation
    private void startAnimation() {
        // Setting up a timer to repaint the welcome animation panel
        int delay = 500; // Delay in milliseconds
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeHangmanPanel.repaint();
            }
        });
        timer.start();
    }

    // Method to choose a random word from the provided word array
    private void chooseRandomWord(String[] array) {
        // Randomly selecting a word and initializing guessedWord
        Random random = new Random();
        int index = random.nextInt(array.length);
        secretWord = array[index];

        guessedWord = new char[secretWord.length()];
        for (int i = 0; i < secretWord.length(); i++) {
            guessedWord[i] = '_';
        }


        updateWordLabel();
        createLetterButtons();
    }

    // Method to create letter buttons for guessing
    private void createLetterButtons() {
        // Removing existing buttons and creating new letter buttons
        buttonPanel.removeAll();

        letterButtons = new JButton[26];
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            letterButtons[i] = new JButton(String.valueOf(letter));
            letterButtons[i].setPreferredSize(new Dimension(40, 40));
            letterButtons[i].addActionListener(new GuessListener());
        }

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        for (JButton letterButton : letterButtons) {
            buttonPanel.add(letterButton);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // Method to update the word label with guessed letters
    private void updateWordLabel() {
        // Updating the word label text and centering it
        StringBuilder builder = new StringBuilder();
        for (char c : guessedWord) {
            builder.append(c);
            builder.append(' ');
        }
        wordLabel.setText(builder.toString());
        // Center align the text
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Remove any existing layout manager from the wordLabel's parent
        wordLabel.getParent().setLayout(null);

        // Get the size of the parent container
        Dimension parentSize = wordLabel.getParent().getSize();

        // Get the size of the wordLabel
        Dimension labelSize = wordLabel.getPreferredSize();

        // Calculate the x and y positions to center the wordLabel
        int x = (parentSize.width - labelSize.width) / 2;
        int y = (parentSize.height - labelSize.height) / 2;

        // Set the position of the wordLabel
        wordLabel.setBounds(x, y, labelSize.width, labelSize.height);

    }

    // ActionListener for the Start button
    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Switching from welcome panel to the main game panel
            remove(welcomePanel);
            add(mainPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    // ActionListener for selecting word arrays (Animals or Foods)
    private class ArraySelectionListener implements ActionListener {
        private String[] array;

        public ArraySelectionListener(String[] array) {
            this.array = array;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Handling the selection of word arrays
            JButton source = (JButton) e.getSource();
            source.setEnabled(false);

            chooseRandomWord(array);
            arraySelected = true;
        }
    }

    // ActionListener for guessing letters
    private class GuessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Handling the guessing of letters, checking correctness,
            // updating game state, and checking for win/loss conditions
            if (!arraySelected) {
                JOptionPane.showMessageDialog(null, "Please select an array first.");
                return;
            }

            JButton source = (JButton) e.getSource();
            source.setEnabled(false);

            char guess = source.getText().charAt(0);
            boolean found = false;
            for (int i = 0; i < secretWord.length(); i++) {
                if (secretWord.charAt(i) == guess) {
                    guessedWord[i] = guess;
                    found = true;
                }
            }

            if (!found) {
                attemptsLeft--;
                incorrectGuesses++;
            }

            updateWordLabel();
            drawHangman();


            if (new String(guessedWord).equals(secretWord)) {
                JOptionPane.showMessageDialog(null, "Congratulations! You won!");
                resetGame();
            }

            if (attemptsLeft == 0) {
                JOptionPane.showMessageDialog(null, "Game over! You lost! The word was: " + secretWord);
                resetGame();
            }
        }
    }

    // Method to reset the game
    private void resetGame() {
        // Prompting the user to play again or exit
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    // Method to restart the game
    private void restartGame() {
        dispose(); // Close the current instance of the game

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main(); // Create a new instance of the game
            }
        });
    }

    // Method to draw the Hangman figure during the game
    private void drawHangman() {

        // Drawing Hangman body parts based on incorrect guesses
        Graphics2D g2d = (Graphics2D) buttonPanel.getGraphics();

        // Draw head
        g2d.setStroke(new BasicStroke(10.0f));

        // Draw head
        if (incorrectGuesses >= 1) {
            g2d.drawLine(900, 600, 1280, 600); // base
            g2d.drawLine(1080, 100, 1080, 600); // vertical
            g2d.drawLine(1080, 100, 1180, 100); // top horizontal
            g2d.drawLine(1180, 100, 1180, 150); // rope
            g2d.drawOval(1130, 150, 100, 100); // head
        }

        // Draw body
        if (incorrectGuesses >= 2) {
            g2d.drawLine(1180, 250, 1180, 400);
        }

        // Draw right leg
        if (incorrectGuesses >= 3) {
            g2d.drawLine(1180, 400, 1120, 500);
        }

        // Draw left leg
        if (incorrectGuesses >= 4) {
            g2d.drawLine(1180, 400, 1240, 500);
        }

        // Draw right arm
        if (incorrectGuesses >= 5) {
            g2d.drawLine(1120, 280, 1180, 350);        }

        // Draw left arm
        if (incorrectGuesses >= 6) {
            g2d.drawLine(1180, 350, 1240, 280);
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main(); // Creating an instance of the game
            }
        });
    }
}
