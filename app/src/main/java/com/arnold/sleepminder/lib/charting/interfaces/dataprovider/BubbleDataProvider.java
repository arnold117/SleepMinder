package com.arnold.sleepminder.lib.charting.interfaces.dataprovider;

import com.arnold.sleepminder.lib.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
