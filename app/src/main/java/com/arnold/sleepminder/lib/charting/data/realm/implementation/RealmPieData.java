package com.arnold.sleepminder.lib.charting.data.realm.implementation;

import com.arnold.sleepminder.lib.charting.data.PieData;
import com.arnold.sleepminder.lib.charting.data.realm.base.RealmUtils;
import com.arnold.sleepminder.lib.charting.interfaces.datasets.IPieDataSet;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmPieData extends PieData {

    public RealmPieData(RealmResults<? extends RealmObject> result, String xValuesField, IPieDataSet dataSet) {
        super(RealmUtils.toXVals(result, xValuesField), dataSet);
    }
}
