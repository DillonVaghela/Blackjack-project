package com.mygdx.game.Domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class DealerAndLoggerAndResultTest {

    private Dealer dealer = new Dealer();


    @Test
    public void computeHitTrue() {
        Logger logger = new Logger();
        logger.addMove(true);
        logger.addMove(false);
        logger.addResult(true, true);
        logger.addResult(false, false);
        assertTrue(dealer.computeHit(logger));
    }

    @Test
    public void computeHitFalse() {
        Logger logger = new Logger();
        logger.addMove(true);
        logger.addMove(false);
        logger.addResult(true, false);
        logger.addResult(false, true);
        logger.addResult(true, true);
        logger.addResult(false, false);
        assertFalse(dealer.computeHit(logger));
    }
}