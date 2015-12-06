
package org.onebeartoe.data.visualization.serial.plotter;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;

/**
 * @author Roberto Marquez
 */
public class SerialPlotterAreaChart extends AreaChart<Number, Number>
{

    public SerialPlotterAreaChart(Axis<Number> xAxis, Axis<Number> yAxis)
    {
        super(xAxis, yAxis);
    }

}
