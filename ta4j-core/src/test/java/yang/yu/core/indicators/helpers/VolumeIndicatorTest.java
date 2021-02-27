/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2019 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package yang.yu.core.indicators.helpers;

import org.junit.Test;
import yang.yu.core.Bar;
import yang.yu.core.Indicator;
import yang.yu.core.BarSeries;
import yang.yu.core.indicators.AbstractIndicatorTest;
import yang.yu.core.mocks.MockBar;
import yang.yu.core.mocks.MockBarSeries;
import yang.yu.core.num.Num;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static yang.yu.core.TestUtils.assertNumEquals;

public class VolumeIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    public VolumeIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void indicatorShouldRetrieveBarVolume() {
        BarSeries series = new MockBarSeries(numFunction);
        VolumeIndicator volumeIndicator = new VolumeIndicator(series);
        for (int i = 0; i < 10; i++) {
            assertEquals(volumeIndicator.getValue(i), series.getBar(i).getVolume());
        }
    }

    @Test
    public void sumOfVolume() {
        List<Bar> bars = new ArrayList<Bar>();
        bars.add(new MockBar(0, 10, numFunction));
        bars.add(new MockBar(0, 11, numFunction));
        bars.add(new MockBar(0, 12, numFunction));
        bars.add(new MockBar(0, 13, numFunction));
        bars.add(new MockBar(0, 150, numFunction));
        bars.add(new MockBar(0, 155, numFunction));
        bars.add(new MockBar(0, 160, numFunction));
        VolumeIndicator volumeIndicator = new VolumeIndicator(new MockBarSeries(bars), 3);

        assertNumEquals(10, volumeIndicator.getValue(0));
        assertNumEquals(21, volumeIndicator.getValue(1));
        assertNumEquals(33, volumeIndicator.getValue(2));
        assertNumEquals(36, volumeIndicator.getValue(3));
        assertNumEquals(175, volumeIndicator.getValue(4));
        assertNumEquals(318, volumeIndicator.getValue(5));
        assertNumEquals(465, volumeIndicator.getValue(6));
    }
}
