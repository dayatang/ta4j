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
package ta4jexamples;

import yang.yu.core.*;
import yang.yu.core.analysis.criteria.AverageProfitableTradesCriterion;
import yang.yu.core.analysis.criteria.RewardRiskRatioCriterion;
import yang.yu.core.analysis.criteria.TotalProfitCriterion;
import yang.yu.core.analysis.criteria.VersusBuyAndHoldCriterion;
import yang.yu.core.base.BaseBarSeriesManager;
import yang.yu.core.base.BaseStrategy;
import yang.yu.core.indicators.SMAIndicator;
import yang.yu.core.indicators.helpers.ClosePriceIndicator;
import yang.yu.core.num.Num;
import yang.yu.core.trading.rules.CrossedDownIndicatorRule;
import yang.yu.core.trading.rules.CrossedUpIndicatorRule;
import yang.yu.core.trading.rules.StopGainRule;
import yang.yu.core.trading.rules.StopLossRule;
import ta4jexamples.loaders.CsvTradesLoader;

/**
 * Quickstart for ta4j.
 *
 * Global example.
 */
public class Quickstart {

    public static void main(String[] args) {

        // Getting a bar series (from any provider: CSV, web service, etc.)
        BarSeries series = CsvTradesLoader.loadBitstampSeries();

        // Getting the close price of the bars
        Num firstClosePrice = series.getBar(0).getClosePrice();
        System.out.println("First close price: " + firstClosePrice.doubleValue());
        // Or within an indicator:
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        // Here is the same close price:
        System.out.println(firstClosePrice.isEqual(closePrice.getValue(0))); // equal to firstClosePrice

        // Getting the simple moving average (SMA) of the close price over the last 5
        // bars
        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
        // Here is the 5-bars-SMA value at the 42nd index
        System.out.println("5-bars-SMA value at the 42nd index: " + shortSma.getValue(42).doubleValue());

        // Getting a longer SMA (e.g. over the 30 last bars)
        SMAIndicator longSma = new SMAIndicator(closePrice, 30);

        // Ok, now let's building our trading rules!

        // Buying rules
        // We want to buy:
        // - if the 5-bars SMA crosses over 30-bars SMA
        // - or if the price goes below a defined price (e.g $800.00)
        Rule buyingRule = new CrossedUpIndicatorRule(shortSma, longSma)
                .or(new CrossedDownIndicatorRule(closePrice, 800));

        // Selling rules
        // We want to sell:
        // - if the 5-bars SMA crosses under 30-bars SMA
        // - or if the price loses more than 3%
        // - or if the price earns more than 2%
        Rule sellingRule = new CrossedDownIndicatorRule(shortSma, longSma)
                .or(new StopLossRule(closePrice, series.numOf(3))).or(new StopGainRule(closePrice, series.numOf(2)));

        // Running our juicy trading strategy...
        BarSeriesManager seriesManager = new BaseBarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(new BaseStrategy(buyingRule, sellingRule));
        System.out.println("Number of trades for our strategy: " + tradingRecord.getTradeCount());

        // Analysis

        // Getting the profitable trades ratio
        AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();
        System.out.println("Profitable trades ratio: " + profitTradesRatio.calculate(series, tradingRecord));
        // Getting the reward-risk ratio
        AnalysisCriterion rewardRiskRatio = new RewardRiskRatioCriterion();
        System.out.println("Reward-risk ratio: " + rewardRiskRatio.calculate(series, tradingRecord));

        // Total profit of our strategy
        // vs total profit of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
        System.out.println("Our profit vs buy-and-hold profit: " + vsBuyAndHold.calculate(series, tradingRecord));

        // Your turn!
    }
}
