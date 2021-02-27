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
package yang.yu.core.analysis.criteria;

import yang.yu.core.BarSeries;
import yang.yu.core.Trade;
import yang.yu.core.TradingRecord;
import yang.yu.core.Num;

/**
 * Number of losing trades criterion.
 */
public class NumberOfLosingTradesCriterion extends AbstractAnalysisCriterion {

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        long numberOfLosingTrades = tradingRecord.getTrades().stream().filter(Trade::isClosed)
                .filter(trade -> isLosingTrade(series, trade)).count();
        return series.numOf(numberOfLosingTrades);
    }

    private boolean isLosingTrade(BarSeries series, Trade trade) {
        if (trade.isClosed()) {
            Num exitPrice = series.getBar(trade.getExit().getIndex()).getClosePrice();
            Num entryPrice = series.getBar(trade.getEntry().getIndex()).getClosePrice();

            Num profit = exitPrice.minus(entryPrice).multipliedBy(trade.getExit().getAmount());
            return profit.isNegative();
        }
        return false;
    }

    @Override
    public Num calculate(BarSeries series, Trade trade) {
        return isLosingTrade(series, trade) ? series.numOf(1) : series.numOf(0);
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }
}
