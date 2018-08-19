import java.util.*;

public class Block {

    public String magicByte;
    public int size;
    public BlockHeader header;
    public int txCount;
    public List<Transaction> transactions = new ArrayList<>();
}
