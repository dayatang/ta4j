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
package ta4jexamples.backtesting;

import yang.yu.core.backtest.BacktestExecutor;
import yang.yu.core.base.BaseStrategy;
import yang.yu.core.Indicator;
import yang.yu.core.Order;
import yang.yu.core.Rule;
import yang.yu.core.Strategy;
import yang.yu.core.BarSeries;
import yang.yu.core.indicators.SMAIndicator;
import yang.yu.core.indicators.helpers.ClosePriceIndicator;
import yang.yu.core.Num;
import yang.yu.core.num.PrecisionNum;
import yang.yu.core.trading.rules.OverIndicatorRule;
import yang.yu.core.trading.rules.UnderIndicatorRule;
import ta4jexamples.loaders.CsvBarsLoader;

import java.util.ArrayList;
import java.util.List;

public class SimpleMovingAverageRangeBacktest {

    public static void main(String[] args) {
        BarSeries series = CsvBarsLoader.loadAppleIncSeries();

        int start = 3;
        int stop = 50;
        int step = 5;

        final List<Strategy> strategies = new ArrayList<>();
        for (int i = start; i <= stop; i += step) {
            Strategy strategy = new BaseStrategy("Sma(" + i + ")", createEntryRule(series, i),
                    createExitRule(series, i));
            strategies.add(strategy);
        }
        BacktestExecutor backtestExecutor = new BacktestExecutor(series);
        backtestExecutor.execute(strategies, PrecisionNum.valueOf(50), Order.OrderType.BUY);
    }

    private static Rule createEntryRule(BarSeries series, int barCount) {
        Indicator<Num> closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, barCount);
        return new UnderIndicatorRule(sma, closePrice);
    }

    private static Rule createExitRule(BarSeries series, int barCount) {
        Indicator<Num> closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, barCount);
        return new OverIndicatorRule(sma, closePrice);
    }
}
