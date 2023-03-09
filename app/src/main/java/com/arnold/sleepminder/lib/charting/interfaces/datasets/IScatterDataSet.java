package com.arnold.sleepminder.lib.charting.interfaces.datasets;

import com.arnold.sleepminder.lib.charting.charts.ScatterChart;
import com.arnold.sleepminder.lib.charting.data.Entry;

/**
 * Created by philipp on 21/10/15.
 */
public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry> {

    /**
     * Returns the currently set scatter shape size
     *
     * @return
     */
    float getScatterShapeSize();

    /**
     * Returns all the different scattershapes the chart uses
     *
     * @return
     */
    ScatterChart.ScatterShape getScatterShape();

    /**
     * Returns radius of the hole in the shape
     *
     * @return
     */
    float getScatterShapeHoleRadius();

    /**
     * Returns the color for the hole in the shape
     *
     * @return
     */
    int getScatterShapeHoleColor();
}
