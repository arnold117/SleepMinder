
package com.arnold.sleepminder.lib.charting.buffer;

import com.arnold.sleepminder.lib.charting.data.Entry;
import com.arnold.sleepminder.lib.charting.interfaces.datasets.IScatterDataSet;

public class ScatterBuffer extends AbstractBuffer<IScatterDataSet> {
    
    public ScatterBuffer(int size) {
        super(size);
    }

    protected void addForm(float x, float y) {
        buffer[index++] = x;
        buffer[index++] = y;
    }

    @Override
    public void feed(IScatterDataSet data) {
        
        float size = data.getEntryCount() * phaseX;
        
        for (int i = 0; i < size; i++) {

            Entry e = data.getEntryForIndex(i);
            addForm(e.getXIndex(), e.getVal() * phaseY);
        }
        
        reset();
    }
}
