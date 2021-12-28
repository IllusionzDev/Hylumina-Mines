public class Chat {
    public static String watermark = new GetConfigData(Main.GetInstance().config).getStringFromConfig("hylumina.prefix") + " ";

    private Player ply;
    private String message;

    public Chat(Player ply, String message) {
        this.ply = ply;
        this.message = message;
    }

    public void WatermarkMessage() {
        ply.sendMessage(watermark + message);
    }

    public void message() {
        ply.sendMessage(message);
    }
}
