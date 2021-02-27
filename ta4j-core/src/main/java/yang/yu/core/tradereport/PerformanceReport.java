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
package yang.yu.core.tradereport;

import yang.yu.core.Num;

/**
 * This class represents report which contains performance statistics
 */
public class PerformanceReport {

    private final Num totalProfitLoss;
    private final Num totalProfitLossPercentage;
    private final Num totalProfit;
    private final Num totalLoss;

    public PerformanceReport(Num totalProfitLoss, Num totalProfitLossPercentage, Num totalProfit, Num totalLoss) {
        this.totalProfitLoss = totalProfitLoss;
        this.totalProfitLossPercentage = totalProfitLossPercentage;
        this.totalProfit = totalProfit;
        this.totalLoss = totalLoss;
    }

    public Num getTotalProfitLoss() {
        return totalProfitLoss;
    }

    public Num getTotalProfitLossPercentage() {
        return totalProfitLossPercentage;
    }

    public Num getTotalProfit() {
        return totalProfit;
    }

    public Num getTotalLoss() {
        return totalLoss;
    }

}