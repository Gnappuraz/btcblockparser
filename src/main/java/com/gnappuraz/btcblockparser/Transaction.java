package com.gnappuraz.btcblockparser;

import java.util.*;

class Transaction {

    public int version;
    public int inputCount;
    public List<Input> inputs = new ArrayList<>();
    public int outputCount;
    public List<Output> outputs = new ArrayList<>();
    public int lockTime;
}
