import java.nio.file.*;
import java.io.*;
import java.util.*;

class Main {
    public static void main(String[] args) throws Exception {

        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = Files.newInputStream(Paths.get("blocks.dat"), StandardOpenOption.READ);
            bis = new BufferedInputStream(is);

            BitcoinParser parser = new BitcoinParser(bis);
            List<Block> blocks = parser.parse(10);

            int i = 0;
            for (Block b : blocks) {
                System.out.println("::::::::::::::: BLOCK " + i++ + " :::::::::::::::::");
                System.out.println(JSONPrinter.print(b));
            }

            bis.close();

        } finally {
            if(bis != null) bis.close();
            else if(is != null) is.close();
        }
    }
}
