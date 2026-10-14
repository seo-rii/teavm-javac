import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;

public class Main {
    enum Color {
        RED,
        GREEN,
        BLUE
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(Color.class.getEnumConstants()));
        System.out.println(EnumSet.allOf(Color.class));
        System.out.println(EnumSet.complementOf(EnumSet.of(Color.GREEN)));
        System.out.println(Color.valueOf("BLUE").ordinal());
        var map = new EnumMap<Color, Integer>(Color.class);
        map.put(Color.BLUE, 3);
        map.put(Color.RED, 1);
        System.out.println(map);
    }
}
