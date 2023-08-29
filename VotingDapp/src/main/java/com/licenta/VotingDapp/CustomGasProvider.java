package com.licenta.VotingDapp;

import org.web3j.tx.gas.StaticGasProvider;
import java.math.BigInteger;

public class CustomGasProvider extends StaticGasProvider {
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(6_000_000);
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);

    public CustomGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}
