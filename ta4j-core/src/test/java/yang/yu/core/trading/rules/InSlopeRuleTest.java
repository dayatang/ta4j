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
package yang.yu.core.trading.rules;

import org.junit.Before;
import org.junit.Test;
import yang.yu.core.BarSeries;
import yang.yu.core.base.BaseBarSeries;
import yang.yu.core.Indicator;
import yang.yu.core.indicators.helpers.FixedDecimalIndicator;
import yang.yu.core.Num;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InSlopeRuleTest {

    private InSlopeRule rulePositiveSlope;
    private InSlopeRule ruleNegativeSlope;

    @Before
    public void setUp() {
        BarSeries series = new BaseBarSeries();
        Indicator<Num> indicator = new FixedDecimalIndicator(series, 50, 70, 80, 90, 99, 60, 30, 20, 10, 0);
        rulePositiveSlope = new InSlopeRule(indicator, series.numOf(20), series.numOf(30));
        ruleNegativeSlope = new InSlopeRule(indicator, series.numOf(-40), series.numOf(-20));
    }

    @Test
    public void isSatisfied() {
        assertFalse(rulePositiveSlope.isSatisfied(0));
        assertTrue(rulePositiveSlope.isSatisfied(1));
        assertFalse(rulePositiveSlope.isSatisfied(2));
        assertFalse(rulePositiveSlope.isSatisfied(9));

        assertFalse(ruleNegativeSlope.isSatisfied(0));
        assertFalse(ruleNegativeSlope.isSatisfied(1));
        assertTrue(ruleNegativeSlope.isSatisfied(5));
        assertFalse(ruleNegativeSlope.isSatisfied(9));
    }
}
