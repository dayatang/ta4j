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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import yang.yu.core.num.DoubleNum;
import yang.yu.core.cost.LinearBorrowingCostModel;
import yang.yu.core.cost.LinearTransactionCostModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static yang.yu.core.Num.NaN;
import static yang.yu.core.TestUtils.assertNumEquals;

public class TradeTest {

    private Trade newTrade, uncoveredTrade, trEquals1, trEquals2, trNotEquals1, trNotEquals2;

    private CostModel transactionModel;
    private CostModel holdingModel;
    private Order enter;
    private Order exitSameType;
    private Order exitDifferentType;

    @Before
    public void setUp() {
        this.newTrade = new Trade();
        this.uncoveredTrade = new Trade(Order.OrderType.SELL);

        trEquals1 = new Trade();
        trEquals1.operate(1);
        trEquals1.operate(2);

        trEquals2 = new Trade();
        trEquals2.operate(1);
        trEquals2.operate(2);

        trNotEquals1 = new Trade(Order.OrderType.SELL);
        trNotEquals1.operate(1);
        trNotEquals1.operate(2);

        trNotEquals2 = new Trade(Order.OrderType.SELL);
        trNotEquals2.operate(1);
        trNotEquals2.operate(2);

        transactionModel = new LinearTransactionCostModel(0.01);
        holdingModel = new LinearBorrowingCostModel(0.001);

        enter = Order.buyAt(1, DoubleNum.valueOf(2), DoubleNum.valueOf(1), transactionModel);
        exitSameType = Order.sellAt(2, DoubleNum.valueOf(2), DoubleNum.valueOf(1), transactionModel);
        exitDifferentType = Order.buyAt(2, DoubleNum.valueOf(2), DoubleNum.valueOf(1));
    }

    @Test
    public void whenNewShouldCreateBuyOrderWhenEntering() {
        newTrade.operate(0);
        assertEquals(Order.buyAt(0, NaN, NaN), newTrade.getEntry());
    }

    @Test
    public void whenNewShouldNotExit() {
        assertFalse(newTrade.isOpened());
    }

    @Test
    public void whenOpenedShouldCreateSellOrderWhenExiting() {
        newTrade.operate(0);
        newTrade.operate(1);
        assertEquals(Order.sellAt(1, NaN, NaN), newTrade.getExit());
    }

    @Test
    public void whenClosedShouldNotEnter() {
        newTrade.operate(0);
        newTrade.operate(1);
        assertTrue(newTrade.isClosed());
        newTrade.operate(2);
        assertTrue(newTrade.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void whenExitIndexIsLessThanEntryIndexShouldThrowException() {
        newTrade.operate(3);
        newTrade.operate(1);
    }

    @Test
    public void shouldCloseTradeOnSameIndex() {
        newTrade.operate(3);
        newTrade.operate(3);
        assertTrue(newTrade.isClosed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenOrderTypeIsNull() {
        new Trade(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenOrdersHaveSameType() {
        new Trade(Order.buyAt(0, NaN, NaN), Order.buyAt(1, NaN, NaN));
    }

    @Test
    public void whenNewShouldCreateSellOrderWhenEnteringUncovered() {
        uncoveredTrade.operate(0);
        assertEquals(Order.sellAt(0, NaN, NaN), uncoveredTrade.getEntry());
    }

    @Test
    public void whenOpenedShouldCreateBuyOrderWhenExitingUncovered() {
        uncoveredTrade.operate(0);
        uncoveredTrade.operate(1);
        assertEquals(Order.buyAt(1, NaN, NaN), uncoveredTrade.getExit());
    }

    @Test
    public void overrideToString() {
        assertEquals(trEquals1.toString(), trEquals2.toString());
        assertNotEquals(trEquals1.toString(), trNotEquals1.toString());
        assertNotEquals(trEquals1.toString(), trNotEquals2.toString());
    }

    @Test
    public void testEqualsForNewTrades() {
        assertEquals(newTrade, new Trade());
        assertNotEquals(newTrade, new Object());
        assertNotEquals(newTrade, null);
    }

    @Test
    public void testEqualsForEntryOrders() {
        Trade trLeft = newTrade;
        Trade trRightEquals = new Trade();
        Trade trRightNotEquals = new Trade();

        Assert.assertEquals(Order.OrderType.BUY, trRightNotEquals.operate(2).getType());
        assertNotEquals(trLeft, trRightNotEquals);

        Assert.assertEquals(Order.OrderType.BUY, trLeft.operate(1).getType());
        Assert.assertEquals(Order.OrderType.BUY, trRightEquals.operate(1).getType());
        assertEquals(trLeft, trRightEquals);

        assertNotEquals(trLeft, trRightNotEquals);
    }

    @Test
    public void testEqualsForExitOrders() {
        Trade trLeft = newTrade;
        Trade trRightEquals = new Trade();
        Trade trRightNotEquals = new Trade();

        Assert.assertEquals(Order.OrderType.BUY, trLeft.operate(1).getType());
        Assert.assertEquals(Order.OrderType.BUY, trRightEquals.operate(1).getType());
        Assert.assertEquals(Order.OrderType.BUY, trRightNotEquals.operate(1).getType());

        Assert.assertEquals(Order.OrderType.SELL, trRightNotEquals.operate(3).getType());
        assertNotEquals(trLeft, trRightNotEquals);

        Assert.assertEquals(Order.OrderType.SELL, trLeft.operate(2).getType());
        Assert.assertEquals(Order.OrderType.SELL, trRightEquals.operate(2).getType());
        assertEquals(trLeft, trRightEquals);

        assertNotEquals(trLeft, trRightNotEquals);
    }

    @Test
    public void testGetProfitForBuyStartingType() {
        Trade trade = new Trade(Order.OrderType.BUY);

        trade.operate(0, DoubleNum.valueOf(10.00), DoubleNum.valueOf(2));
        trade.operate(0, DoubleNum.valueOf(12.00), DoubleNum.valueOf(2));

        final Num profit = trade.getProfit();

        assertEquals(DoubleNum.valueOf(4.0), profit);
    }

    @Test
    public void testGetProfitForSellStartingType() {
        Trade trade = new Trade(Order.OrderType.SELL);

        trade.operate(0, DoubleNum.valueOf(12.00), DoubleNum.valueOf(2));
        trade.operate(0, DoubleNum.valueOf(10.00), DoubleNum.valueOf(2));

        final Num profit = trade.getProfit();

        assertEquals(DoubleNum.valueOf(4.0), profit);
    }

    @Test
    public void testCostModelConsistencyTrue() {
        new Trade(enter, exitSameType, transactionModel, holdingModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCostModelEntryInconsistent() {
        new Trade(enter, exitDifferentType, CostModel.ZERO, holdingModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCostModelExitInconsistent() {
        new Trade(enter, exitDifferentType, transactionModel, holdingModel);
    }

    @Test
    public void getProfitLongNoFinalBarTest() {
        Trade closedTrade = new Trade(enter, exitSameType, transactionModel, holdingModel);
        Trade openTrade = new Trade(Order.OrderType.BUY, transactionModel, holdingModel);
        openTrade.operate(5, DoubleNum.valueOf(100), DoubleNum.valueOf(1));

        Num profitOfClosedTrade = closedTrade.getProfit();
        Num proftOfOpenTrade = openTrade.getProfit();

        assertNumEquals(DoubleNum.valueOf(-0.04), profitOfClosedTrade);
        assertNumEquals(DoubleNum.valueOf(0), proftOfOpenTrade);
    }

    @Test
    public void getProfitLongWithFinalBarTest() {
        Trade closedTrade = new Trade(enter, exitSameType, transactionModel, holdingModel);
        Trade openTrade = new Trade(Order.OrderType.BUY, transactionModel, holdingModel);
        openTrade.operate(5, DoubleNum.valueOf(2), DoubleNum.valueOf(1));

        Num profitOfClosedTrade = closedTrade.getProfit(10, DoubleNum.valueOf(12));
        Num profitOfOpenTrade = openTrade.getProfit(10, DoubleNum.valueOf(12));

        assertNumEquals(DoubleNum.valueOf(9.98), profitOfOpenTrade);
        assertNumEquals(DoubleNum.valueOf(-0.04), profitOfClosedTrade);
    }

    @Test
    public void getProfitShortNoFinalBarTest() {
        Order sell = Order.sellAt(1, DoubleNum.valueOf(2), DoubleNum.valueOf(1), transactionModel);
        Order buyBack = Order.buyAt(10, DoubleNum.valueOf(2), DoubleNum.valueOf(1), transactionModel);

        Trade closedTrade = new Trade(sell, buyBack, transactionModel, holdingModel);
        Trade openTrade = new Trade(Order.OrderType.SELL, transactionModel, holdingModel);
        openTrade.operate(5, DoubleNum.valueOf(100), DoubleNum.valueOf(1));

        Num profitOfClosedTrade = closedTrade.getProfit();
        Num proftOfOpenTrade = openTrade.getProfit();

        Num expectedHoldingCosts = DoubleNum.valueOf(2.0 * 9.0 * 0.001);
        Num expectedProfitOfClosedTrade = DoubleNum.valueOf(-0.04).minus(expectedHoldingCosts);

        assertNumEquals(expectedProfitOfClosedTrade, profitOfClosedTrade);
        assertNumEquals(DoubleNum.valueOf(0), proftOfOpenTrade);
    }

    @Test
    public void getProfitShortWithFinalBarTest() {
        Order sell = Order.sellAt(1, DoubleNum.valueOf(2), DoubleNum.valueOf(1), transactionModel);
        Order buyBack = Order.buyAt(10, DoubleNum.valueOf(2), DoubleNum.valueOf(1), transactionModel);

        Trade closedTrade = new Trade(sell, buyBack, transactionModel, holdingModel);
        Trade openTrade = new Trade(Order.OrderType.SELL, transactionModel, holdingModel);
        openTrade.operate(5, DoubleNum.valueOf(2), DoubleNum.valueOf(1));

        Num profitOfClosedTradeFinalAfter = closedTrade.getProfit(20, DoubleNum.valueOf(3));
        Num profitOfOpenTradeFinalAfter = openTrade.getProfit(20, DoubleNum.valueOf(3));
        Num profitOfClosedTradeFinalBefore = closedTrade.getProfit(5, DoubleNum.valueOf(3));
        Num profitOfOpenTradeFinalBefore = openTrade.getProfit(5, DoubleNum.valueOf(3));

        Num expectedHoldingCosts = DoubleNum.valueOf(2.0 * 9.0 * 0.001);
        Num expectedProfitOfClosedTrade = DoubleNum.valueOf(-0.04).minus(expectedHoldingCosts);

        assertNumEquals(DoubleNum.valueOf(-1.05), profitOfOpenTradeFinalAfter);
        assertNumEquals(DoubleNum.valueOf(-1.02), profitOfOpenTradeFinalBefore);
        assertNumEquals(expectedProfitOfClosedTrade, profitOfClosedTradeFinalAfter);
        assertNumEquals(expectedProfitOfClosedTrade, profitOfClosedTradeFinalBefore);
    }
}
