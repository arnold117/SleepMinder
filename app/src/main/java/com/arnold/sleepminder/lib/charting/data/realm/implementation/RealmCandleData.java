package com.arnold.sleepminder.lib.charting.data.realm.implementation;

import com.arnold.sleepminder.lib.charting.data.CandleData;
import com.arnold.sleepminder.lib.charting.data.realm.base.RealmUtils;
import com.arnold.sleepminder.lib.charting.interfaces.datasets.ICandleDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmCandleData extends CandleData {

    public RealmCandleData(RealmResults<? extends RealmObject> result, String xValuesField, List<ICandleDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
