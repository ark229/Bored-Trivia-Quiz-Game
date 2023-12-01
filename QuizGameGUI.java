import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class QuizGameGUI {
    private JFrame frame;
    private JLabel questionLabel, timerLabel;
    private JRadioButton[] options;
    private ButtonGroup optionGroup;
    private JButton nextButton, restartButton, startButton;
    private Timer quizTimer;
    private JPanel quizPanel, introPanel;
    private String[] questions;
    private String[][] choices;
    private String[] answers;
    private int currentQuestion = 0;
    private int score = 0;
    private int timeLeft = 10;

    public QuizGameGUI() {
        initializeQuizContent();
        shuffleQuestions();

        frame = new JFrame("Bored Trivia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        createIntroPanel();
        createQuizPanel();

        frame.add(introPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void createIntroPanel() {
        introPanel = new JPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
        introPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Welcome to Bored Trivia");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        JLabel instructionLabel1 = new JLabel("If you're bored, come test your knowledge.");
        instructionLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel instructionLabel2 = new JLabel("You'll have 10 seconds to answer each question.");
        instructionLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionPanel.add(instructionLabel1);
        instructionPanel.add(instructionLabel2);

        startButton = new JButton("Start Quiz");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(introPanel);
                frame.add(quizPanel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
                setupTimer(); // Start the timer here
            }
        });

        introPanel.add(titleLabel);
        introPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        introPanel.add(instructionPanel);
        introPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        introPanel.add(startButton);
    }

    private void createQuizPanel() {
        quizPanel = new JPanel();
        quizPanel.setLayout(new BorderLayout());

        questionLabel = new JLabel("<html>" + questions[currentQuestion] + "</html>");
        quizPanel.add(questionLabel, BorderLayout.NORTH);

        options = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        JPanel optionPanel = new JPanel(new GridLayout(4, 1));
        for (int i = 0; i < options.length; i++) {
            options[i] = new JRadioButton(choices[currentQuestion][i]);
            optionGroup.add(options[i]);
            optionPanel.add(options[i]);
        }
        quizPanel.add(optionPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNext();
            }
        });
        bottomPanel.add(nextButton);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartQuiz();
            }
        });
        bottomPanel.add(restartButton);

        timerLabel = new JLabel("Time left: " + timeLeft);
        bottomPanel.add(timerLabel);

        quizPanel.add(bottomPanel, BorderLayout.SOUTH);

        setupTimer();
    }

    private void initializeQuizContent() {
        questions = new String[] {
                "What is the size of an integer in Java?",
                "Where was the first example of paper money used?",
                "Who is generally considered the inventor of the motor car?",
                "What Italian city is famous for its system of canals?",
                "What is the national sport of Canada?",
                "In what year were the first Air Jordan sneakers released?",
                "If you were looking at Iguazu Falls, on what continent would you be?",
                "What spirit is used in making a Tom Collins?",
                "The fear of insects is known as what?",
                "Which Game of Thrones character is known as the Young Wolf?"

        };
        choices = new String[][] {
                { "A: 32 bits", "B: 16 bits", "C: 64 bits", "D: 8 bits" },
                { "A: China", "B: Turkey", "C: Greece", "D: Rome" },
                { "A: Henry Ford", "B: Karl Benz", "C: Henry M. Leland", "D: Alexander Winton" },
                { "A: Rome", "B: Naples", "C: Venice", "D: Comacchio" },
                { "A: Hockey", "B: Curling", "C: Skiing", "D: Lacrosse" },
                { "A: 1985", "B: 1984", "C: 1987", "D: 1990" },
                { "A: Asia", "B: South America", "C: Africa", "D: North America" },
                { "A: Vodka", "B: Rum", "C: Wiskey", "D: Gin" },
                { "A: Entomophobia", "B: Ailurophobia", "C: Arachnophobia", "D: Enochlophobia" },
                { "A: Arya Stark", "B: Sansa Stark", "C: Robb Stark", "D: Eddard Stark" }
        };
        answers = new String[] { "A", "A", "B", "C", "D", "B", "B", "D", "A", "C" };
    }

    private void shuffleQuestions() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        String[] shuffledQuestions = new String[questions.length];
        String[][] shuffledChoices = new String[choices.length][];
        String[] shuffledAnswers = new String[answers.length];

        for (int i = 0; i < indices.size(); i++) {
            int index = indices.get(i);
            shuffledQuestions[i] = questions[index];
            shuffledChoices[i] = choices[index];
            shuffledAnswers[i] = answers[index];
        }

        questions = shuffledQuestions;
        choices = shuffledChoices;
        answers = shuffledAnswers;
    }

    private void setupTimer() {
        if (quizTimer != null) {
            quizTimer.stop(); // Stop the existing timer if it's running
        }
        quizTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Time left: " + timeLeft);
                } else {
                    handleNext();
                }
            }
        });
        // quizTimer.start();
    }

    private void handleNext() {
        boolean isCorrect = checkAnswer(); // Check the answer and store the result
        String message = isCorrect ? "CORRECT!" : "WRONG!";
        JOptionPane.showMessageDialog(frame, message); // Show a dialog with the result

        currentQuestion++;
        if (currentQuestion < questions.length) {
            updateQuestionView();
            timeLeft = 10;
            quizTimer.restart();
        } else {
            quizTimer.stop();
            int result = JOptionPane.showConfirmDialog(frame, "Quiz complete! Your score: "
                    + String.format("%.2f%%", (double) score / questions.length * 100) + "\nWould you like to restart?",
                    "Quiz Finished", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                restartQuiz();
            } else {
                frame.dispose();
            }
        }
    }

    private void restartQuiz() {
        currentQuestion = 0;
        score = 0;
        timeLeft = 10;
        shuffleQuestions();
        updateQuestionView();
        quizTimer.restart();
    }

    private void updateQuestionView() {
        questionLabel.setText("<html>" + questions[currentQuestion] + "</html>");
        optionGroup.clearSelection();
        for (int i = 0; i < options.length; i++) {
            options[i].setText(choices[currentQuestion][i]);
        }
    }

    private boolean checkAnswer() {
        int selectedIndex = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                selectedIndex = i;
                break;
            }
        }
        if (selectedIndex != -1) {
            char selectedAnswer = (char) ('A' + selectedIndex);
            if (String.valueOf(selectedAnswer).equalsIgnoreCase(answers[currentQuestion])) {
                score++;
                return true; // Correct answer
            }
        }
        return false; // Incorrect answer
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizGameGUI();
            }
        });
    }
}
