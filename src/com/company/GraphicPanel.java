package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;



public class GraphicPanel extends JPanel {
    int N = 1, a = 0, solutionN = 4096;
    double b = 0.5, h;
    private int width;
    private int height;
    private final Color graphicColorApproximately = Color.RED;
    private final Color graphicColorSolution = Color.BLUE;

    public void paint(Graphics g)
    {
        super.paint(g);
        width = getWidth();
        height = getHeight();

        drawGrid(g);
        drawAxis(g);
        drawGraphic(g);
    }

    //создаем сетку
    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        for(int x=width/2 - 100; x<width; x+=200)
            g.drawLine(x, 0, x, height);

        for(int x=width/2 - 100; x>0; x-=200)
            g.drawLine(x, 0, x, height);

        for(int y=height/40; y<height; y+=200)
            g.drawLine(0, y, width, y);

        for(int y=height/40; y>0; y-=200)
            g.drawLine(0, y, width, y);
    }

    //Создаем оси
    private void drawAxis(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(width/2 - 100, 0, width/2 - 100, height);
        g.drawLine(0, height/40, width, height/40);
    }

    //Дифферениальное уравнение
    public double funcDiff(double x, double y){
        return -y*Math.tan(x)-4*y*y*Math.sin(x);
    }

    //Решение задачи Коши
    public double funcSolution(double x){
        return -Math.cos(x)/Math.cos(2*x);
    }

    //Считаем точки точного решения
    public Point2D.Double[] eulierSolution(){
        Point2D.Double [] points = new Point2D.Double[solutionN];
        points[0] = new Point2D.Double(0,-1);
        for(int i = 1; i < solutionN; i++)
            points[i] = new Point2D.Double(points[i-1].getX()+(b-a)/solutionN, funcSolution(points[i-1].getX()+(b-a)/solutionN));

        return points;
    }

    //Считаем узлы приближенного решения методом Эйлера
    public Point2D.Double[] eulier() {
        Point2D.Double [] points = new Point2D.Double[N];
        points[0] = new Point2D.Double(0, -1);
        for(int i = 1; i < N; i++)
            points[i] = new Point2D.Double(points[i-1].getX() + h, points[i-1].getY() + h*funcDiff(points[i-1].getX(), points[i-1].getY()));

        return points;
    }

    //Считаем максимальную невязку (она достигается в последнем узле)
    public double maxDiscrepancy() {
        Point2D.Double[] dotApproximate = eulier();
        double dotSolution = funcSolution(b);

        return Math.abs(Math.abs(dotSolution) - Math.abs(dotApproximate[N - 1].y));
    }

    private void drawGraphic(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        g.setColor(graphicColorApproximately);

        Point2D.Double[] dotApproximate = eulier();

        int sizeEl = 2;

        //Строим узлы приближенного решения
        for(int i = 0; i < N ; i++) {
            Ellipse2D.Double shape = new Ellipse2D.Double(400*dotApproximate[i].getX()+(double)width/2 - 100-sizeEl,
                    -400*dotApproximate[i].getY()+(double)height/40-sizeEl, sizeEl, sizeEl);
            g.draw(shape);
        }

        //Соединяем узлы
        for (int i = 0; i < N - 1; i++)
            g.draw(new Line2D.Double(400*dotApproximate[i].getX()+(double)width/2  - 100, -400*dotApproximate[i].getY()+(double)height/40,
                    400*dotApproximate[i+1].getX()+(double)width/2 - 100, -400*dotApproximate[i+1].getY()+(double)height/40));

        //Строим точный график
        g.setColor(graphicColorSolution);
        Point2D.Double[] dotSolution = eulierSolution();

        for (int i = 0; i < solutionN - 1; i++)
            g.draw(new Line2D.Double(400*dotSolution[i].getX()+(double)width/2 - 100, -400*dotSolution[i].getY()+(double)height/40,
                    400*dotSolution[i+1].getX()+(double)width/2 - 100, -400*dotSolution[i+1].getY()+(double)height/40));

    }

    //Меняет N и перестраивает график приближенного решения
    public void setSplit(int N) {
        this.N = N;
        h = (b-a)/(N-1);
        repaint();
    }
}
