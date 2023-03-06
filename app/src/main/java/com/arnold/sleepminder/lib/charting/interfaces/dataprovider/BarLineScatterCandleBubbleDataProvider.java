package com.arnold.sleepminder.lib.charting.interfaces.dataprovider;

import com.arnold.sleepminder.lib.charting.components.YAxis.AxisDependency;
import com.arnold.sleepminder.lib.charting.data.BarLineScatterCandleBubbleData;
import com.arnold.sleepminder.lib.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
