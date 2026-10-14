import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        var input = new ByteArrayInputStream("1 2\nk\u00f6ln\n".getBytes(StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        var first = reader.readLine();
        var second = reader.readLine();
        System.out.println(first + " | " + second.length() + ":" + (int) second.charAt(1) + " | " + reader.readLine());

        var tokenizer = new StreamTokenizer(new StringReader("3 4 5"));
        int sum = 0;
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            sum += (int) tokenizer.nval;
        }
        System.out.println("sum=" + sum);

        var bytes = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(bytes, StandardCharsets.UTF_8)) {
            writer.write("caf\u00e9");
        }
        System.out.println(bytes.size() + " " + "caf\u00e9".getBytes(StandardCharsets.UTF_8).length);

        var chars = new char[2];
        CharBuffer.wrap(new char[] { 'o', 'k' }).get(chars);
        var allocated = CharBuffer.allocate(2).put(chars);
        allocated.flip();
        System.out.println(allocated);
        System.out.println(ByteBuffer.wrap(new byte[] { 0, 0, 0, 7 }).getInt() + ByteBuffer.allocate(4).getInt());
        System.out.println(ShortBuffer.wrap(new short[] { 1 }).get(0)
                + IntBuffer.allocate(1).put(0, 2).get(0)
                + LongBuffer.wrap(new long[] { 3 }).get(0)
                + FloatBuffer.allocate(1).put(0, 4.5f).get(0)
                + DoubleBuffer.wrap(new double[] { 5.25 }).get(0));
    }
}
