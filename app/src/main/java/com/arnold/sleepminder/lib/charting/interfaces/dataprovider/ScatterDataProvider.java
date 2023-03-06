package com.arnold.sleepminder.lib.charting.interfaces.dataprovider;

import com.arnold.sleepminder.lib.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
