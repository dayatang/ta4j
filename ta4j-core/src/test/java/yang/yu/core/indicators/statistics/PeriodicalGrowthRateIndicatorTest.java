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
package yang.yu.core.indicators.statistics;

import org.junit.Before;
import org.junit.Test;
import yang.yu.core.*;
import yang.yu.core.base.BaseBarSeriesManager;
import yang.yu.core.base.BaseStrategy;
import yang.yu.core.indicators.AbstractIndicatorTest;
import yang.yu.core.indicators.helpers.ClosePriceIndicator;
import yang.yu.core.mocks.MockBarSeries;
import yang.yu.core.num.Num;
import yang.yu.core.trading.rules.CrossedDownIndicatorRule;
import yang.yu.core.trading.rules.CrossedUpIndicatorRule;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static yang.yu.core.TestUtils.assertNumEquals;
import static yang.yu.core.num.NaN.NaN;

public class PeriodicalGrowthRateIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeriesManager seriesManager;

    private ClosePriceIndicator closePrice;

    public PeriodicalGrowthRateIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries mockSeries = new MockBarSeries(numFunction, 29.49, 28.30, 27.74, 27.65, 27.60, 28.70, 28.60, 28.19,
                27.40, 27.20, 27.28, 27.00, 27.59, 26.20, 25.75, 24.75, 23.33, 24.45, 24.25, 25.02, 23.60, 24.20, 24.28,
                25.70, 25.46, 25.10, 25.00, 25.00, 25.85);
        seriesManager = new BaseBarSeriesManager(mockSeries);
        closePrice = new ClosePriceIndicator(mockSeries);
    }

    @Test
    public void testGetTotalReturn() {
        PeriodicalGrowthRateIndicator gri = new PeriodicalGrowthRateIndicator(this.closePrice, 5);
        Num result = gri.getTotalReturn();
        assertNumEquals(0.9564, result);
    }

    @Test
    public void testCalculation() {
        PeriodicalGrowthRateIndicator gri = new PeriodicalGrowthRateIndicator(this.closePrice, 5);

        assertEquals(gri.getValue(0), NaN);
        assertEquals(gri.getValue(4), NaN);
        assertNumEquals(-0.0268, gri.getValue(5));
        assertNumEquals(0.0541, gri.getValue(6));
        assertNumEquals(-0.0495, gri.getValue(10));
        assertNumEquals(0.2009, gri.getValue(21));
        assertNumEquals(0.0220, gri.getValue(24));
        assertEquals(gri.getValue(25), NaN);
        assertEquals(gri.getValue(26), NaN);
    }

    @Test
    public void testStrategies() {

        PeriodicalGrowthRateIndicator gri = new PeriodicalGrowthRateIndicator(this.closePrice, 5);

        // Rules
        Rule buyingRule = new CrossedUpIndicatorRule(gri, 0);
        Rule sellingRule = new CrossedDownIndicatorRule(gri, 0);

        Strategy strategy = new BaseStrategy(buyingRule, sellingRule);

        // Check trades
        int result = seriesManager.run(strategy).getTradeCount();
        int expResult = 3;

        assertEquals(expResult, result);
    }
}
