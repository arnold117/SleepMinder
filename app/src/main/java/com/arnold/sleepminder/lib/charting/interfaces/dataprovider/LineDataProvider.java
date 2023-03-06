package com.arnold.sleepminder.lib.charting.interfaces.dataprovider;

import com.arnold.sleepminder.lib.charting.components.YAxis;
import com.arnold.sleepminder.lib.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
