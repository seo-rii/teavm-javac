public class Main {
    public static void main(String[] args) {
        System.out.println(String.format("%.2f", 1.5));
        System.out.printf("%.3f\n", 2.0 / 3);
        System.out.println("%8.3f|%-6.1f|".formatted(Math.PI, -2.5));
    }
}
