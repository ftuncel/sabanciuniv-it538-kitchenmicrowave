package edu.sabanciuniv.kitchenmicrowavesimulation.UI;

import edu.sabanciuniv.kitchenmicrowavesimulation.KitchenMicrowave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KitchenMicrowaveUI {
    private JFrame frame;
    private JButton startButton;
    private JButton stopButton;
    private JButton hatchButton;
    private JLabel stateLabel;
    private JTextField timerField;
    private Timer timer;
    private boolean isHatchOpen = false;
    private KitchenMicrowave kmsSM = KitchenMicrowave.getInstance();

    public KitchenMicrowaveUI() {
        initStateLabel();
        initStartButton();
        initStopButton();
        initTimerTextField();
        initHatchButton();
        initMainFrame();
    }

    public static void main(String[] args) {
        KitchenMicrowaveUI kms = new KitchenMicrowaveUI();
    }

    private void initStartButton() {
        startButton = new JButton("Start");
        startButton.setSize(50,50);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kmsSM.setTimer(Integer.parseInt(timerField.getText()));
                kmsSM.handleEvent(KitchenMicrowave.Event.cooking_started);
                printState();
                timer.start();
            }
        });
    }

    private void initStopButton() {
        stopButton = new JButton("Stop");
        stopButton.setSize(50,50);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kmsSM.handleEvent(KitchenMicrowave.Event.countdown_cancelled);
                printState();
            }
        });
    }

    private void initTimerTextField() {
        timerField = new JTextField("30", 4);
        timerField.setHorizontalAlignment(JTextField.CENTER);
        timerField.setFont(new java.awt.Font("Arial", Font.ITALIC | Font.BOLD, 30));
        timerField.setEditable(true);
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTimerField();
                if (kmsSM.getState() == KitchenMicrowave.State.COOKING) {
                    updateTimerField();
                }
                else {
                    timer.stop();
                    printState();
                }
            }
        });
    }

    private void initStateLabel() {
        stateLabel = new JLabel();
        stateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        stateLabel.setFont(new java.awt.Font("Arial", Font.PLAIN, 20));
        stateLabel.setBounds(0,0,100,50);
    }

    private void initHatchButton() {
        hatchButton = new JButton("Open Hatch");
        hatchButton.setBackground(Color.GREEN.darker());
        hatchButton.setForeground(Color.WHITE);
        hatchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processHatchButtonClicked();
            }
        });
    }

    private void initMainFrame() {
        frame = new JFrame("KMS Microwave");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        // TOP
        JPanel statePanel = new JPanel();
        statePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statePanel.add(stateLabel);

        // MID-UP
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);

        // MID-DOWN
        JPanel timerPanel = new JPanel();
        timerPanel.setSize(100,50);
        timerPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
        timerPanel.add(timerField);

        //BOTTOM
        JPanel hatchPanel = new JPanel();
        hatchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
        hatchPanel.add(hatchButton, BorderLayout.EAST);

        
        JPanel midPanelGrid1 = new JPanel();
        midPanelGrid1.setSize(100,100);
        midPanelGrid1.setLayout(new BorderLayout());
        midPanelGrid1.add(buttonsPanel, BorderLayout.SOUTH);

        JPanel midPanelGrid2 = new JPanel();
        midPanelGrid2.setSize(100,100);
        midPanelGrid2.setLayout(new BorderLayout());
        midPanelGrid2.add(timerPanel, BorderLayout.NORTH);

        JPanel midPanelGrid = new JPanel();
        midPanelGrid.setLayout(new GridLayout(2,1,15,15));
        midPanelGrid.add(midPanelGrid1);
        midPanelGrid.add(midPanelGrid2);

        frame.add(statePanel, BorderLayout.NORTH);
        frame.add(midPanelGrid, BorderLayout.CENTER);
        frame.add(hatchPanel, BorderLayout.SOUTH);

        printState();
        frame.setVisible(true);
    }

    private void updateTimerField() {
        timerField.setText(Integer.toString(kmsSM.getTimer()));
    }

    private void processHatchButtonClicked(){
        isHatchOpen = !isHatchOpen;
        if (isHatchOpen) {
            hatchButton.setText("Close Hatch");
            hatchButton.setBackground(Color.RED.darker());
            kmsSM.handleEvent(KitchenMicrowave.Event.hatch_opened);
            printState();
            timer.stop();
        } else {
            timer.start();
            hatchButton.setText("Open Hatch");
            hatchButton.setBackground(Color.GREEN.darker());
            kmsSM.setTimer(Integer.parseInt(timerField.getText()));
            kmsSM.handleEvent(KitchenMicrowave.Event.hatch_closed);
            printState();
            timer.start();
        }
    }

    private void printState(){
        switch (kmsSM.getState()){
            case READY:
                this.stateLabel.setForeground(Color.GREEN.darker());
                timerField.setEditable(true);
                break;
            case COOKING:
                this.stateLabel.setForeground(Color.BLUE);
                timerField.setEditable(false);
                break;
            case HATCH_OPEN:
                this.stateLabel.setForeground(Color.darkGray);
                timerField.setEditable(false);
                break;
            case COOKING_PAUSE:
                this.stateLabel.setForeground(Color.RED);
                timerField.setEditable(true);
                break;
        }
        this.stateLabel.setText("State: " + kmsSM.getState());
    }
}