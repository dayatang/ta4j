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
package yang.yu.core.indicators.adx;

import org.junit.Test;
import yang.yu.core.BarSeries;
import yang.yu.core.ExternalIndicatorTest;
import yang.yu.core.Indicator;
import yang.yu.core.TestUtils;
import yang.yu.core.indicators.AbstractIndicatorTest;
import yang.yu.core.indicators.XLSIndicatorTest;
import yang.yu.core.num.Num;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static yang.yu.core.TestUtils.assertIndicatorEquals;

public class MinusDIIndicatorTest extends AbstractIndicatorTest<BarSeries, Num> {

    private ExternalIndicatorTest xls;

    public MinusDIIndicatorTest(Function<Number, Num> nf) {
        super((data, params) -> new MinusDIIndicator(data, (int) params[0]), nf);
        xls = new XLSIndicatorTest(this.getClass(), "ADX.xls", 13, numFunction);
    }

    @Test
    public void xlsTest() throws Exception {
        BarSeries xlsSeries = xls.getSeries();
        Indicator<Num> indicator;

        indicator = getIndicator(xlsSeries, 1);
        assertIndicatorEquals(xls.getIndicator(1), indicator);
        assertEquals(0.0, indicator.getValue(indicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);

        indicator = getIndicator(xlsSeries, 3);
        assertIndicatorEquals(xls.getIndicator(3), indicator);
        assertEquals(21.0711, indicator.getValue(indicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);

        indicator = getIndicator(xlsSeries, 13);
        assertIndicatorEquals(xls.getIndicator(13), indicator);
        assertEquals(20.9020, indicator.getValue(indicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);
    }

}
