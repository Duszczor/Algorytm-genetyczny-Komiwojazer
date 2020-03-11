
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChartEx2 extends JFrame {

    int minRange = 7000;
    int maxRange = 8500;

    public XYDataset createDataset(int x, int y, String nazwa) {

        XYSeries series1 = new XYSeries("nazwa");
        series1.add(x, y);

        //XYSeries series2 = new XYSeries("2016");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        //dataset.addSeries(series2);

        return dataset;
    }

    public JFreeChart createChart(final XYDataset dataset, String axisX, String axisY, String tit) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                tit,
                axisX,
                axisY,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();


        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setRange(minRange, maxRange);

        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(tit,
                        new Font("Serif", Font.BOLD, 20)
                )
        );

        return chart;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LineChartEx2 ex = new LineChartEx2();
            ex.setVisible(true);
        });
    }
}