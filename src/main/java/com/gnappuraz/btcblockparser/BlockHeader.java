package com.gnappuraz.btcblockparser;

import java.time.*;

class BlockHeader {

    public int version;
    public String prevHash;
    public String merkleRoot;
    public LocalDateTime time;
    public String targetBits;
    public int nonce;
}
