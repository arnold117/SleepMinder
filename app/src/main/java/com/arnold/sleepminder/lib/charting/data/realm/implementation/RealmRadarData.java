package com.arnold.sleepminder.lib.charting.data.realm.implementation;

import com.arnold.sleepminder.lib.charting.data.RadarData;
import com.arnold.sleepminder.lib.charting.data.realm.base.RealmUtils;
import com.arnold.sleepminder.lib.charting.interfaces.datasets.IRadarDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmRadarData extends RadarData{

    public RealmRadarData(RealmResults<? extends RealmObject> result, String xValuesField, List<IRadarDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
