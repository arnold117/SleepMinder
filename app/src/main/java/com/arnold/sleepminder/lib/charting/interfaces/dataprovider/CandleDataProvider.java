package com.arnold.sleepminder.lib.charting.interfaces.dataprovider;

import com.arnold.sleepminder.lib.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
