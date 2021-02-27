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

import yang.yu.core.BarSeries;
import yang.yu.core.Num;
import yang.yu.core.indicators.ATRIndicator;
import yang.yu.core.indicators.CachedIndicator;
import yang.yu.core.indicators.MMAIndicator;
import yang.yu.core.indicators.helpers.MinusDMIndicator;

/**
 * -DI indicator. Part of the Directional Movement System
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_directional_index_adx">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_directional_index_adx</a>
 * @see <a
 *      href="https://www.investopedia.com/terms/a/adx.asp>https://www.investopedia.com/terms/a/adx.asp</a>
 */
public class MinusDIIndicator extends CachedIndicator<Num> {

    private final MMAIndicator avgMinusDMIndicator;
    private final ATRIndicator atrIndicator;
    private final int barCount;

    public MinusDIIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.avgMinusDMIndicator = new MMAIndicator(new MinusDMIndicator(series), barCount);
        this.atrIndicator = new ATRIndicator(series, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return avgMinusDMIndicator.getValue(index).dividedBy(atrIndicator.getValue(index)).multipliedBy(numOf(100));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}