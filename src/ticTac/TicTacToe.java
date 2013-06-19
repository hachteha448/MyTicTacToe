package ticTac;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TicTacToe implements ActionListener {
    final String VERSION = "0.7";
    JFrame window = new JFrame("Крестики-нолики" + VERSION);

    JMenuBar mnuMain = new JMenuBar();
    JMenuItem mnuNewGame = new JMenuItem("Новая игра"),
            mnuExit = new JMenuItem("Выход");

    JButton btn1v1 = new JButton("Начать новую игру друг против друга"),
            btnBack = new JButton("<--назад");
    JButton btnEmpty[] = new JButton[10];

    JPanel pnlNewGame = new JPanel(),
            pnlNorth = new JPanel(),
            pnlSouth = new JPanel(),
            pnlTop = new JPanel(),
            pnlBottom = new JPanel(),
            pnlPlayingField = new JPanel();
    JLabel lblTitle = new JLabel("Крестики-Нолики");
    JTextArea txtMessage = new JTextArea();

    //различные возможные выигрышные комбинации по горизонтали, вертикали и диагонали
    final int winCombo[][] = new int[][] {
            {1, 2, 3}, {1, 4, 7}, {1, 5, 9},
            {4, 5, 6}, {2, 5, 8}, {3, 5, 7},
            {7, 8, 9}, {3, 6, 9}
    };
    final int X = 412, Y = 268, color = 190;
    boolean inGame = false;
    boolean win = false;
    boolean btnEmptyClicked = false;
    String message;
    int turn = 1;
    int wonNumber1 = 1, wonNumber2 = 1, wonNumber3 = 1;

    //установка начальных настроек игры и вывода компонентов
    public TicTacToe() {
        //настройка окна
        window.setSize(X, Y);
        window.setLocation(450, 260);
        window.setResizable(false);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //установка свойств менеджеров расположения
        pnlNewGame.setLayout(new GridLayout(2, 1, 2, 10));
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));

        pnlNorth.setBackground(new Color(color-20, color-20, color-20));
        pnlSouth.setBackground(new Color(color, color, color));

        pnlTop.setBackground(new Color(color, color, color));
        pnlBottom.setBackground(new Color(color, color, color));

        pnlTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlNewGame.setBackground(Color.blue);

        //добавление пунктов меню
        mnuMain.add(mnuNewGame);
        mnuMain.add(mnuExit);

        //добавление кнопок на панель для новой игры
        pnlNewGame.add(btn1v1);

        //Добавление слушателей для кнопок и пунктов меню
        mnuNewGame.addActionListener(this);
        mnuExit.addActionListener(this);
        btn1v1.addActionListener(this);
        btnBack.addActionListener(this);

        //настройка игрового поля
        pnlPlayingField.setLayout(new GridLayout(3, 3, 2, 2));
        pnlPlayingField.setBackground(Color.black);
        for(int i=1; i<=9; i++) {
            btnEmpty[i] = new JButton();
            btnEmpty[i].setBackground(new Color(220, 220, 220));
            btnEmpty[i].addActionListener(this);
            pnlPlayingField.add(btnEmpty[i]);
        }

        //добавление панелей
        pnlNorth.add(mnuMain);
        pnlSouth.add(lblTitle);

        //добавление окон
        window.add(pnlNorth, BorderLayout.NORTH);
        window.add(pnlSouth, BorderLayout.CENTER);
        window.setVisible(true);
    }


    public void actionPerformed(ActionEvent click) {
        Object source = click.getSource();
        for(int i=1; i<=9; i++) {
            if(source == btnEmpty[i] && turn < 10) {
                btnEmptyClicked = true;
                if(!(turn % 2 == 0))
                    btnEmpty[i].setText("X");
                else
                    btnEmpty[i].setText("O");
                btnEmpty[i].setEnabled(false);
                pnlPlayingField.requestFocus();
                turn++;
            }
        }
        if(btnEmptyClicked) {
            checkWin();
            btnEmptyClicked = false;
        }
        if(source == mnuNewGame) {
            clearPanelSouth();
            pnlSouth.setLayout(new GridLayout(2, 1, 2, 5));
            pnlTop.add(pnlNewGame);
            pnlBottom.add(btnBack);
            pnlSouth.add(pnlTop);
            pnlSouth.add(pnlBottom);

        }
        else if(source == btn1v1) {
            if(inGame) {
                int option = JOptionPane.showConfirmDialog(null, "Если вы начнёте новую игру" +
                        "то ваша текущая игра будет потеряна..." + "\n" +
                        "Вы хотите продолжить?",
                        "Завершить игру?" ,JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION) {
                    inGame = false;
                }
            }
            if(!inGame) {
                btnEmpty[wonNumber1].setBackground(new Color(220, 220, 220));
                btnEmpty[wonNumber2].setBackground(new Color(220, 220, 220));
                btnEmpty[wonNumber3].setBackground(new Color(220, 220, 220));
                turn = 1;
                for(int i=1; i<10; i++) {
                    btnEmpty[i].setText("");
                    btnEmpty[i].setEnabled(true);
                }
                win = false;
                showGame();

            }
        }
        else if(source == mnuExit) {
            int option = JOptionPane.showConfirmDialog(null, "Вы уверенны что хотите выйти?",
                    "Завершить игру" ,JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
                System.exit(0);
        }
        else if(source == btnBack) {
            if(inGame)
                showGame();
            else {
                clearPanelSouth();
                pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
                pnlNorth.setVisible(true);
                pnlSouth.add(lblTitle);
            }
        }
        pnlSouth.setVisible(false);
        pnlSouth.setVisible(true);
    }

    //показывать игровые поля
    public void showGame() {
        clearPanelSouth();
        inGame = true;
        pnlSouth.setLayout(new BorderLayout());
        pnlSouth.add(pnlPlayingField, BorderLayout.CENTER);
        pnlPlayingField.requestFocus();
    }

    //проверка на выиграшную комбинацию
    public void checkWin() {
        for(int i=0; i<7; i++) {
            if(
                    !btnEmpty[winCombo[i][0]].getText().equals("") &&
                            btnEmpty[winCombo[i][0]].getText().equals(btnEmpty[winCombo[i][1]].getText()) &&
                            btnEmpty[winCombo[i][1]].getText().equals(btnEmpty[winCombo[i][2]].getText())
                ) {
                win = true;
                wonNumber1 = winCombo[i][0];
                wonNumber2 = winCombo[i][1];
                wonNumber3 = winCombo[i][2];
                btnEmpty[wonNumber1].setBackground(Color.white);
                btnEmpty[wonNumber2].setBackground(Color.white);
                btnEmpty[wonNumber3].setBackground(Color.white);
                break;
            }
        }
        if(win || (!win && turn>9)) {
            if(win) {
                if(turn % 2 == 0)
                    message = "X выиграл!";
                else
                    message = "O выиграл!";
                win = false;
            } else if(!win && turn>9) {
                message = "Оба игрока зашли в тупик!\nСыграйте ещё.";
            }
            JOptionPane.showMessageDialog(null, message);
            for(int i=1; i<=9; i++) {
                btnEmpty[i].setEnabled(false);
            }
        }
    }

    //убирает все доступные панели   которые только могут быть
    public void clearPanelSouth() {
        pnlSouth.remove(lblTitle);
        pnlSouth.remove(pnlTop);
        pnlSouth.remove(pnlBottom);
        pnlSouth.remove(pnlPlayingField);
        pnlTop.remove(pnlNewGame);
        pnlTop.remove(txtMessage);
        pnlBottom.remove(btnBack);
    }

    public static void main(String[] args) {
        new TicTacToe();

    }
}
