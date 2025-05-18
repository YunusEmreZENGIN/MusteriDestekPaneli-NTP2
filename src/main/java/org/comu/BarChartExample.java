package org.comu;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BarChartExample extends JFrame {

    public BarChartExample(List<SupportTicket> tickets) {
        setTitle("Son 30 Günde Çözülen Talepler");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> dateCounts = new TreeMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (SupportTicket ticket : tickets) {
            if ("Çözülmüş".equals(ticket.getStatus())) {
                String date = sdf.format(ticket.getCreatedAt());
                dateCounts.put(date, dateCounts.getOrDefault(date, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : dateCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Çözülen", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Son 30 Günde Çözülen Talepler", "Tarih", "Adet", dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
    }
}
