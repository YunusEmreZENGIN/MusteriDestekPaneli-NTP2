package org.comu;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.util.List;

public class PieChartExample extends JFrame {

    public PieChartExample(List<SupportTicket> tickets) {
        setTitle("Kategori Dağılımı");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultPieDataset dataset = new DefaultPieDataset();

        int teknik = 0, yazilim = 0, diger = 0;
        for (SupportTicket ticket : tickets) {
            switch (ticket.getCategory()) {
                case "Teknik Destek" -> teknik++;
                case "Yazılım Hatası" -> yazilim++;
                default -> diger++;
            }
        }

        dataset.setValue("Teknik Destek", teknik);
        dataset.setValue("Yazılım Hatası", yazilim);
        dataset.setValue("Diğer", diger);

        JFreeChart chart = ChartFactory.createPieChart(
                "Kategoriye Göre Talepler", dataset, true, true, false);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }
}
