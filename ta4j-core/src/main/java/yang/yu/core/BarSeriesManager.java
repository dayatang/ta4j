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
package yang.yu.core;

import yang.yu.core.Order.OrderType;

/**
 * A manager for {@link BarSeries} objects.
 *
 * Used for backtesting. Allows to run a {@link Strategy trading strategy} over
 * the managed bar series.
 */
public interface BarSeriesManager {

    /**
     * @return the managed bar series
     */
    BarSeries getBarSeries();

    /**
     * Runs the provided strategy over the managed series.
     *
     * Opens the trades with {@link OrderType} BUY order.
     * 
     * @return the trading record coming from the run
     */
    TradingRecord run(Strategy strategy);

    /**
     * Runs the provided strategy over the managed series (from startIndex to
     * finishIndex).
     *
     * Opens the trades with {@link OrderType} BUY order.
     * 
     * @param strategy    the trading strategy
     * @param startIndex  the start index for the run (included)
     * @param finishIndex the finish index for the run (included)
     * @return the trading record coming from the run
     */
    TradingRecord run(Strategy strategy, int startIndex, int finishIndex);

    /**
     * Runs the provided strategy over the managed series.
     *
     * Opens the trades with the specified {@link OrderType orderType} order.
     * 
     * @param strategy  the trading strategy
     * @param orderType the {@link OrderType} used to open the trades
     * @return the trading record coming from the run
     */
    TradingRecord run(Strategy strategy, OrderType orderType);

    /**
     * Runs the provided strategy over the managed series (from startIndex to
     * finishIndex).
     *
     * Opens the trades with the specified {@link OrderType orderType} order.
     * 
     * @param strategy    the trading strategy
     * @param orderType   the {@link OrderType} used to open the trades
     * @param startIndex  the start index for the run (included)
     * @param finishIndex the finish index for the run (included)
     * @return the trading record coming from the run
     */
    TradingRecord run(Strategy strategy, OrderType orderType, int startIndex, int finishIndex);

    /**
     * Runs the provided strategy over the managed series.
     *
     * @param strategy  the trading strategy
     * @param orderType the {@link OrderType} used to open the trades
     * @param amount    the amount used to open/close the trades
     * @return the trading record coming from the run
     */
    TradingRecord run(Strategy strategy, OrderType orderType, Num amount);

    /**
     * Runs the provided strategy over the managed series (from startIndex to
     * finishIndex).
     *
     * @param strategy    the trading strategy
     * @param orderType   the {@link OrderType} used to open the trades
     * @param amount      the amount used to open/close the trades
     * @param startIndex  the start index for the run (included)
     * @param finishIndex the finish index for the run (included)
     * @return the trading record coming from the run
     */
    TradingRecord run(Strategy strategy, OrderType orderType, Num amount, int startIndex, int finishIndex);
}
