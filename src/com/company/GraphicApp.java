package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class GraphicApp {
    private JFrame frame;
    private JTextField NTextField;
    private JTextField discrepancyTextField;
    private GraphicPanel graphicPanel;

    //Создаем графическое окно
    public GraphicApp(){
        createFrame();
        initElements();
    }

    private void createFrame() {
        frame = new JFrame("Построение решений дифференциального уравнения");
        frame.setSize(1050, 900);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setVisual(){
        frame.setVisible(true);
    }

    private void initElements() {
        Container mainContainer = frame.getContentPane();
        mainContainer.setLayout(new BorderLayout());

        Box leftPanel = createLeftPanel();
        mainContainer.add(leftPanel, BorderLayout.WEST);

        graphicPanel = new GraphicPanel();
        graphicPanel.setBackground(Color.WHITE);
        mainContainer.add(graphicPanel);
    }

    //Создаем панель для ввода разбиения, вывода развязки и кнопки
    private Box createLeftPanel() {
        Box panel = Box.createVerticalBox();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JLabel title = new JLabel("Метод ломанных Эйлера");
        panel.add(title);
        JLabel title2 = new JLabel("(Шаг сетки: 0.5)");
        panel.add(title2);

        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Цвета построенных решений: "));
        panel.add(new JLabel("Синий - точное решение"));
        panel.add(new JLabel("Красный - приближенное решение"));

        panel.add(Box.createVerticalStrut(40));

        panel.add(new JLabel("Задайте разбиение:"));
        NTextField = new JTextField();
        NTextField.setMaximumSize(new Dimension(280, 30));
        panel.add(NTextField);

        panel.add(Box.createVerticalStrut(60));

        panel.add(new JLabel("Максимальная невязка"));
        discrepancyTextField = new JTextField();
        discrepancyTextField.setMaximumSize(new Dimension(280, 30));
        panel.add(discrepancyTextField);

        panel.add(Box.createVerticalGlue());

        JButton button = new JButton("Построить решения");
        panel.add(button);

        panel.add(Box.createVerticalStrut(20));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeN();

                double discrepancy = graphicPanel.maxDiscrepancy();
                discrepancyTextField.setText(String.valueOf(discrepancy));
            }
        });

        return panel;
    }
    private void changeN(){
        int N = Integer.parseInt(NTextField.getText());
        graphicPanel.setSplit(N);
    }
}
