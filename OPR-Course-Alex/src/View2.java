import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;

public class View2 {

    private JFrame frame;
    private JTable table;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private DefaultTableModel model = new DefaultTableModel();
    private String[][] arr;
    private float[][] arrX0, arrY0;
    private double[] ans;
    private double F;
    private boolean min = false;
    private JList<String> list;
    private JLabel label_1, label_4, label_6;
    private double x1F,x2F;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    View2 window = new View2();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public View2() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 746, 468);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        table = new JTable();
        table.setBounds(10, 73, 515, 300);
        frame.getContentPane().add(table);

        JLabel label = new JLabel("Количество ограничений");
        label.setBounds(552, 14, 216, 14);
        frame.getContentPane().add(label);

        textField = new JTextField();
        textField.setColumns(10);
        textField.setBounds(552, 38, 161, 20);
        frame.getContentPane().add(textField);

        JButton button = new JButton("Создать таблицу");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = Integer.parseInt(textField.getText());
                    if (row >= 2)
                        createTable(row);
                    arr = new String[row][4];
                    arrX0 = new float[row][200];
                    arrY0 = new float[row][200];
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Oшибочное значение количества ограничений");
                }
            }
        });
        button.setBounds(552, 69, 161, 23);
        frame.getContentPane().add(button);

        JButton button_1 = new JButton("Рассчитать");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getInfo();
            }
        });
        button_1.setBounds(552, 103, 161, 23);
        frame.getContentPane().add(button_1);

        JButton button_2 = new JButton("Открыть файл");
        button_2.setBounds(552, 137, 161, 23);
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        frame.getContentPane().add(button_2);

        JLabel lblB = new JLabel("B");
        lblB.setBounds(462, 48, 27, 14);
        frame.getContentPane().add(lblB);

        JLabel lblX = new JLabel("X1");
        lblX.setBounds(53, 48, 44, 14);
        frame.getContentPane().add(lblX);

        JLabel label_3 = new JLabel("Целевая функция");
        label_3.setBounds(10, 394, 116, 14);
        frame.getContentPane().add(label_3);

        JLabel lblX_1 = new JLabel("X2");
        lblX_1.setBounds(163, 48, 44, 14);
        frame.getContentPane().add(lblX_1);

        JLabel label_2 = new JLabel(">=   ||   <=");
        label_2.setBounds(302, 48, 72, 14);
        frame.getContentPane().add(label_2);

        JLabel lblX_3 = new JLabel("X1");
        lblX_3.setBounds(128, 394, 44, 14);
        frame.getContentPane().add(lblX_3);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(182, 391, 72, 20);
        frame.getContentPane().add(textField_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(302, 391, 72, 20);
        frame.getContentPane().add(textField_2);

        JLabel lblX_2 = new JLabel("X2");
        lblX_2.setBounds(264, 394, 27, 14);
        frame.getContentPane().add(lblX_2);

        JLabel label_5 = new JLabel("->");
        label_5.setBounds(386, 394, 44, 14);
        frame.getContentPane().add(label_5);

        list = new JList<String>();
        list.setModel(new AbstractListModel<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;
            String[] values = new String[] { "min", "max" };

            public int getSize() {
                return values.length;
            }

            public String getElementAt(int index) {
                return values[index];
            }
        });
        list.setSelectedIndex(0);
        list.setToolTipText("min\r\nmax");
        list.setBounds(411, 380, 78, 38);
        frame.getContentPane().add(list);

        JLabel lblx = new JLabel("Пример: 5x + 3y >=30");
        lblx.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblx.setBounds(21, 0, 378, 28);
        frame.getContentPane().add(lblx);

        JButton button_3 = new JButton("Очистить");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        button_3.setBounds(552, 171, 161, 23);
        frame.getContentPane().add(button_3);

        JLabel lblXminmax = new JLabel("X1min(max)=");
        lblXminmax.setBounds(535, 272, 78, 20);
        frame.getContentPane().add(lblXminmax);

        JLabel lblXminmax_1 = new JLabel("X2min(max)=");
        lblXminmax_1.setBounds(535, 302, 78, 20);
        frame.getContentPane().add(lblXminmax_1);

        JLabel lblFminmax = new JLabel("Fmin(max)=");
        lblFminmax.setBounds(535, 333, 78, 20);
        frame.getContentPane().add(lblFminmax);

        label_1 = new JLabel("");
        label_1.setBounds(622, 272, 78, 20);
        frame.getContentPane().add(label_1);

        label_4 = new JLabel("");
        label_4.setBounds(622, 302, 78, 20);
        frame.getContentPane().add(label_4);

        label_6 = new JLabel("");
        label_6.setBounds(622, 336, 78, 20);
        frame.getContentPane().add(label_6);
    }

    void clear() {
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                table.setValueAt("", i, j);
            }
        }
        textField.setText("");
        textField_1.setText("");
        textField_2.setText("");
        label_1.setText("");
        label_4.setText("");
        label_6.setText("");
    }

    void createTable(int row) {
        table.removeAll();
        table.setModel(model);
        model.setRowCount(row);
        model.setColumnCount(4);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(300 / row);

    }

    void getInfo() {
        try {
            for (int i = 0; i < arr.length; i++) {
                arr[i][0] = model.getValueAt(i, 0).toString();
                arr[i][1] = model.getValueAt(i, 1).toString();
                arr[i][2] = model.getValueAt(i, 2).toString();
                arr[i][3] = model.getValueAt(i, 3).toString();
            }
            getX0(arr);
//            getY0(arr);
            draw();
             x1F = Double.parseDouble(textField_1.getText());
             x2F = Double.parseDouble(textField_2.getText());
            min = list.getSelectedIndex() == 0 ? true : false;
            solve(arr, x1F, x2F);
            new Graph();

        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(frame, "Oшибочные данные таблицы");
        }
    }

    void solve(String[][] arr, double xMin, double yMin) {
        double x1, x2, c;
        String snak;
        LinearProgram lp = new LinearProgram(new double[] { xMin, yMin });
        for (int i = 0; i < arr.length; i++) {
            x1 = Double.parseDouble(arr[i][0]);
            x2 = Double.parseDouble(arr[i][1]);
            c = Double.parseDouble(arr[i][3]);
            snak = arr[i][2];
            if (snak.contains("<="))
                lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[] { x1, x2 }, c, "c" + i));
            else
                lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] { x1, x2 }, c, "c" + i));

        }
        
        lp.setMinProblem(!min);
        LinearProgramSolver solver = SolverFactory.newDefault();
        ans = solver.solve(lp);
        for (double d : ans)
            System.out.println(d);
        F = xMin * ans[0] + yMin * ans[1];
        System.out.println(F);
        label_1.setText(ans[0] + "");
        label_4.setText(ans[1] + "");
        label_6.setText(F + "");
    }

    void getX0(String[][] temp) {
        
        float x, y, b;
        for (int i = 0; i < temp.length; i++) {
            String[] str = temp[i];
            x = Float.parseFloat(str[0]);
            y = Float.parseFloat(str[1]);
            b = Float.parseFloat(str[3]);
            for (int j = 0; j < 200; j++) {
//                float xTemp = j-100f;
//                float xWithTemp=xTemp*x;
//                float chisl=b-xWithTemp;
//                float arrtemp=chisl/y;
                arrX0[i][j] = (b - x * (j - 100f)) / y;
                arrX0[i][j] = arrX0[i][j]*25;
                

            }
            
        }
    }


    void paint(Graphics gr, float[][] arrX02, float[][] arrY02) {
        gr.setColor(Color.red);
        gr.drawLine(0, 500, 1000, 500);
        gr.drawLine(500, 0, 500, 1000);
        for(int i=0; i<1000;i=i+100) {
            String str=(i-500)/25+"";
            
            gr.drawChars(str.toCharArray(), 0, str.toCharArray().length, i, 500);            
        }
        for(int i=0; i<1000;i=i+100) {
            String str=(-1*(i-500)/25)+"";
            
            gr.drawChars(str.toCharArray(), 0, str.toCharArray().length, 500, i);            
        }
        
        gr.setColor(Color.BLACK);
        int x1, y1, x2, y2;
        for (int i = 0; i < arrX02.length; i++) {
            for (int j = 0; j < 199; j++) {
                x1 = j - 100;
                y1 = (int) arrX02[i][j];
                x2 = j - 99;
                y2 = (int) arrX02[i][j + 1];

                gr.drawLine(500 + x1*25, 500 - y1, 500 + x2*25, 500 - y2);
            }
        }
        gr.setColor(Color.BLUE);
        int x1FF=(int) (-x1F*-500/x2F);
        int x2FF=(int) (-x1F*500/x2F);
        gr.drawLine(0, 500 - x1FF, 1000, 500 - x2FF);

    }

    void draw() {
        BufferedImage im = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics gr = im.getGraphics();
        paint(gr,arrX0,arrY0);

        try {
            ImageIO.write(im, "png", new File("big.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    void openFile() {
        //строки файла:
        //число строк, Х1 огранб Х2огран
        //х1 х2 >= b
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");                
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
             Scanner sc = null;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             int row = sc.nextInt();
             arr = new String[row][4];
             arrX0 = new float[row][200];
             arrY0 = new float[row][200];
             x1F = sc.nextFloat();
             textField_1.setText(x1F+"");
             x2F = sc.nextFloat();
             textField_2.setText(x2F+"");
//             String[] temp;
             createTable(row);
             list.setSelectedIndex(sc.next().contains("min")?0:1);
             min = list.getSelectedIndex() == 0 ? true : false;
             for(int i=0; i<row;i++) {
//                     String tmp=sc.next();
//                     temp = tmp.split(" ");                    
                     table.setValueAt(sc.next(), i, 0);
                     table.setValueAt(sc.next(), i, 1);
                     table.setValueAt(sc.next(), i, 2);
                     table.setValueAt(sc.next(), i, 3);
                 
             }
        }
    }
}
