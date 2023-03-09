package com.arnold.sleepminder.lib.charting.data.realm.implementation;

import com.arnold.sleepminder.lib.charting.data.realm.base.RealmUtils;
import com.arnold.sleepminder.lib.charting.data.BubbleData;
import com.arnold.sleepminder.lib.charting.interfaces.datasets.IBubbleDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmBubbleData extends BubbleData {

    public RealmBubbleData(RealmResults<? extends RealmObject> result, String xValuesField, List<IBubbleDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
