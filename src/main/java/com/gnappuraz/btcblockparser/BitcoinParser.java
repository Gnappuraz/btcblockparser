package com.gnappuraz.btcblockparser;

import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.time.*;

class BitcoinParser {

    BufferedInputStream s;

    public BitcoinParser(BufferedInputStream s){
        this.s = s;
    }

    public List<Block> parse(int blocks) throws Exception {

        int b;
        int i = 0;

        List<Block> result = new ArrayList<Block>();

        while (i < blocks) {

            Block block = new Block();

            // Block prelude
            block.magicByte = "0x "+f(r(4));
            block.size = btoi(r(4));

            // Block header
            BlockHeader header = new BlockHeader();
            block.header = header;
            header.version = btoi(r(4));

            header.prevHash = "0x "+f(r(32));
            header.merkleRoot = "0x "+f(r(32));
            header.time = LocalDateTime.ofInstant(Instant.ofEpochSecond(btoi(r(4))), ZoneId.systemDefault());
            header.targetBits = "0x "+f(r(4));
            header.nonce = btoi(r(4));

            // Block body
            block.txCount = rvi();
            for(int z = 0; z < block.txCount; z++) {
                Transaction tx = new Transaction();
                tx.version = btoi(r(4));
                tx.inputCount = rvi();
                for(int j = 0; j < tx.inputCount; j++){
                    Input input = new Input();
                    input.txid = "0x "+f(r(32));
                    input.vout = btoi(r(4));
                    input.scriptSigSize = rvi();
                    input.scriptSig = "0x "+f(r(input.scriptSigSize));
                    input.sequence = "0x "+f(r(4));
                    tx.inputs.add(input);
                }
                tx.outputCount = rvi();
                for(int j = 0; j < tx.inputCount; j++){
                    Output output = new Output();
                    output.value = btol(r(8));
                    output.scriptPubKeySize = rvi();
                    output.scriptPubKey = "0x "+f(r(output.scriptPubKeySize));
                    tx.outputs.add(output);
                }
                tx.lockTime = btoi(r(4));
                block.transactions.add(tx);
            }

            result.add(block);
            i++;
        }

        return result;
    }

    private static int btoi(byte[] b){
        int res = 0;
        for (int i = 0; i < 4; i++) {
            res += (b[i] & 0xff) << (i*8);
        }
        return res;
    }

    private static long btol(byte[] b){
        long res = 0;
        for (int i = 0; i < 8; i++) {
            res += (b[i] & 0xff) << (i*8);
        }
        return res;
    }

    private byte[] r(int size) throws Exception {

        byte[] b = new byte[size];
        s.read(b, 0, size);
        return b;
    }

    private int rvi() throws Exception {
        int b1 = (s.read() & 0xff);

        if(b1 <= 0xfc){
            return b1;
        } else {
            int size = (2<<(b1 - 0xfc -1) & 0xff);
            return btoi(r(size));
        }
    }

    private static String f(byte[] b) {

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < b.length; i++){
            sb.append(String.format("%02X", b[i]));
        }

        return sb.toString();
    }
}
