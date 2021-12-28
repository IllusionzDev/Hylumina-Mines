public class LogMe {
    private static final String consoleWatermark = "[+] HyluminaMines Log - ";

    private final static String RESET = "\u001B[0m";
    private final static String RED = "\u001B[31m";
    private final static String GREEN = "\u001B[32m";
    private final static String YELLOW = "\u001B[33m";
    private final static String YELLOW_BACKGROUND = "\u001B[43m";

    private String message;

    public LogMe(String message) {
        this.message = message;
    }

    public void Normal() {
        System.out.println(consoleWatermark + correctText(message) + RESET);
    }

    public void Success() {
        System.out.println(GREEN + consoleWatermark + correctText(message) + RESET);
    }

    public void Error() {
        System.out.println(RED + consoleWatermark + correctText(message) + RESET);
    }

    public void Warning() {
        System.out.println(YELLOW + consoleWatermark + correctText(message) + RESET);
    }

    private String correctText(String text) {
        if (text.length() > 45) {
            return text.substring(0, 35) + "\n" + text.substring(35);
        }

        return text;
    }
}
