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
package yang.yu.core.indicators;

import yang.yu.core.BarSeries;
import yang.yu.core.Indicator;
import yang.yu.core.Num;
import yang.yu.core.indicators.helpers.HighPriceIndicator;
import yang.yu.core.indicators.helpers.LowPriceIndicator;
import yang.yu.core.indicators.helpers.LowestValueIndicator;

import static yang.yu.core.Num.NaN;

/**
 * Aroon down indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon">chart_school:technical_indicators:aroon</a>
 */
public class AroonDownIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final LowestValueIndicator lowestMinPriceIndicator;
    private final Indicator<Num> minValueIndicator;
    private final Num hundred;

    /**
     * Constructor.
     *
     * @param minValueIndicator the indicator for the maximum price (default
     *                          {@link HighPriceIndicator})
     * @param barCount          the time frame
     */
    public AroonDownIndicator(Indicator<Num> minValueIndicator, int barCount) {
        super(minValueIndicator);
        this.barCount = barCount;
        this.minValueIndicator = minValueIndicator;
        this.hundred = numOf(100);
        // + 1 needed for last possible iteration in loop
        lowestMinPriceIndicator = new LowestValueIndicator(minValueIndicator, barCount + 1);
    }

    /**
     * Default Constructor that is using the maximum price
     *
     * @param series   the bar series
     * @param barCount the time frame
     */
    public AroonDownIndicator(BarSeries series, int barCount) {
        this(new LowPriceIndicator(series), barCount);
    }

    @Override
    protected Num calculate(int index) {
        if (getBarSeries().getBar(index).getLowPrice().isNaN())
            return NaN;

        // Getting the number of bars since the lowest close price
        int endIndex = Math.max(0, index - barCount);
        int nbBars = 0;
        for (int i = index; i > endIndex; i--) {
            if (minValueIndicator.getValue(i).isEqual(lowestMinPriceIndicator.getValue(index))) {
                break;
            }
            nbBars++;
        }

        return numOf(barCount - nbBars).dividedBy(numOf(barCount)).multipliedBy(hundred);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}