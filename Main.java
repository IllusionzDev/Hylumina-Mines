public final class Main extends JavaPlugin {
    private static Main instance = null;
    private WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

    public CreateConfig config, mines;

    @Override
    public void onEnable() {
        new LogMe("Starting...").Warning();
        instance = this;

        if (this.worldEdit == null) {
            new LogMe("WorldEdit not found...").Error();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        config = new CreateConfig("config.yml", "Hylumina/Mines");
        mines = new CreateConfig("mines.yml", "Hylumina/Mines");
        new ConfigStates(config).ConfigDefaults();
        new ConfigStates(mines).LoadConfig();

        SetupCommands();
        SetupListeners();

        new LogMe("Startup successful!").Success();
    }

    @Override
    public void onDisable() {
        new LogMe("Saving data...").Warning();
        new ConfigStates(mines).SaveConfig();
    }

    public static Main GetInstance() {
        return instance;
    }

    public WorldEditPlugin GetWorldEdit() {
        return this.worldEdit;
    }

    private void SetupCommands() {
        getCommand("hm").setExecutor(new Mines());
    }

    private void SetupListeners() {
        String packageName = getClass().getPackage().getName();
        for (Class<?> cl : new Reflections(packageName + ".Events").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) cl.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
