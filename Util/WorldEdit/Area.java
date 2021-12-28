public class Area {
    private static Map<String, String> areaWorlds = Maps.newHashMap();
    private static Map<String, String> areaFirstPositions = Maps.newHashMap();
    private static Map<String, String> areaSecondPositions = Maps.newHashMap();
    private static Map<String, String> areaBlocks = Maps.newHashMap();

    private String areaName;
    private String worldName;
    private String blockName;
    private Player ply;

    public Area(String areaName) {
        this.areaName = areaName;
    }

    public Area(String areaName, Player ply) {
        this.areaName = areaName;
        this.ply = ply;
    }

    public Area(String areaName, String blockName) {
        this.areaName = areaName;
        this.blockName = blockName;
    }

    public Area(String areaName, Player ply, String worldName) {
        this.areaName = areaName;
        this.ply = ply;
        this.worldName = worldName;
    }

    public static Map<String, String> GetAreaWorlds() {
        return areaWorlds;
    }

    public static Map<String, String> GetAreaFirstPositions() {
        return areaFirstPositions;
    }

    public static Map<String, String> GetAreaSecondPositions() {
        return areaSecondPositions;
    }

    public static Map<String, String> GetAreaBlocks() {
        return areaBlocks;
    }

    public static Set<String> GetAreas() {
        return GetAreaWorlds().keySet();
    }

    public boolean CreateArea() {
        BukkitPlayer bukkitPly = Main.GetInstance().GetWorldEdit().wrapPlayer(ply);

        try {
            Region areaRegion = Main.GetInstance().GetWorldEdit().getSession(ply).getSelection(bukkitPly.getWorld());

            if (AreaCreated(areaName)) {
                new LogMe("Area name has already been created.").Error();
                return false;
            }

            CuboidRegion newRegion = new CuboidRegion(areaRegion.getWorld(), areaRegion.getMinimumPoint(), areaRegion.getMaximumPoint());

            SetArea(areaName, worldName, newRegion.getPos1(), newRegion.getPos2());
            new LogMe("New area created '" + areaName + "'.").Success();
            return true;
        }
        catch (IncompleteRegionException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean AddBlock() {
        if (!AreaCreated(areaName)) {
            new LogMe("Area '" + areaName + "' doesn't exist.").Error();
            return false;
        }

        if (!GetAreaBlocks().containsKey(areaName)) {
            AddBlock(areaName, blockName + ",");
            new LogMe("Added block '" + blockName + "' to area '" + areaName + ".");
            return true;
        }

        String getBlockList = GetAreaBlocks().get(areaName);
        AddBlock(areaName, getBlockList + blockName + ",");
        new LogMe("Added block '" + blockName + "' to area '" + areaName + ".");
        return true;
    }

    public boolean RemoveBlock() {
        if (!AreaCreated(areaName)) {
            new LogMe("Area '" + areaName + "' doesn't exist.").Error();
            return false;
        }

        if (!GetAreaBlocks().containsKey(areaName)) {
            new LogMe("Area '" + areaName + "' doesn't have any blocks.").Error();
            return false;
        }

        String blockList = GetAreaBlocks().get(areaName);

        if (!blockList.contains(blockName + ",")) {
            new LogMe("Area '" + areaName + "' doesn't have any '" + blockName + "'.").Error();
            return false;
        }

        RemoveBlock(areaName, blockName + ",");
        new LogMe("Removed block '" + blockName + "' from area '" + areaName + ".");
        return true;
    }

    public boolean RemoveArea() {
        if (!AreaCreated(areaName)) {
            new LogMe("Area '" + areaName + "' doesn't exist.").Error();
            return false;
        }

        RemoveArea(areaName);
        new LogMe("The area '" + areaName + "' has been deleted.").Success();
        return true;
    }

    public boolean ResetArea() {
        if (!AreaCreated(areaName)) {
            new LogMe("Area '" + areaName + "' hasn't been created.").Error();
            return false;
        }

        if (!GetAreaBlocks().containsKey(areaName)) {
            new LogMe("Area '" + areaName + "' doesn't have any blocks to reset with.").Error();
            return false;
        }

        if (new Area(areaName).GetRegion() == null) {
            new LogMe("Unable to grab cuboid region for '" + areaName + "'.").Error();
            return false;
        }

        CuboidRegion region = new Area(areaName).GetRegion();
        EditSession session = Main.GetInstance().GetWorldEdit().getWorldEdit().newEditSession(region.getWorld());
        Reset(areaName, region, session);
        return true;
    }

    public boolean AreaCreated(String areaName) {
        return GetAreaWorlds().containsKey(areaName);
    }

    public CuboidRegion GetRegion() {
        String areaWorld = GetAreaWorlds().get(areaName);
        String areaFirstPos = GetAreaFirstPositions().get(areaName);
        String areaSecondPos = GetAreaSecondPositions().get(areaName);

        String[] firstPositions = areaFirstPos.split("\\s*,\\s*");
        String[] secondPositions = areaSecondPos.split("\\s*,\\s*");

        BlockVector3 positionOne = BlockVector3.at(Integer.parseInt(firstPositions[0]), Integer.parseInt(firstPositions[1]), Integer.parseInt(firstPositions[2]));
        BlockVector3 positionTwo = BlockVector3.at(Integer.parseInt(secondPositions[0]), Integer.parseInt(secondPositions[1]), Integer.parseInt(secondPositions[2]));
        World positionWorld = BukkitAdapter.adapt(Bukkit.getWorld(areaWorld));

        CuboidRegionSelector selection = new CuboidRegionSelector(positionWorld, positionOne, positionTwo);

        try {
            return selection.getRegion();
        } catch (IncompleteRegionException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private boolean SetArea(String areaName, String worldName, BlockVector3 firstPos, BlockVector3 secondPos) {
        try {
            GetAreaWorlds().put(areaName, worldName);
            GetAreaFirstPositions().put(areaName, firstPos.toParserString());
            GetAreaSecondPositions().put(areaName, secondPos.toParserString());
            return true;
        }
        catch (NullPointerException | ClassCastException |
                UnsupportedOperationException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean RemoveArea(String areaName) {
        try {
            GetAreaWorlds().remove(areaName);
            GetAreaFirstPositions().remove(areaName);
            GetAreaSecondPositions().remove(areaName);
            return true;
        }
        catch (NullPointerException | ClassCastException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean Reset(String areaName, CuboidRegion region, EditSession session) {
        String[] blockList = GetAreaBlocks().get(areaName).split("\\s*,\\s*");
        RandomPattern pattern = new RandomPattern();

        for (String blockName : blockList) {
            BlockState blockState = BukkitAdapter.adapt(Material.getMaterial(blockName).createBlockData());
            pattern.add(blockState, 1);
        }

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            Location playerLocation = onlinePlayers.getLocation();

            if (region.contains(BlockVector3.at(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ()))) {
                Location teleportPlayer = new Location(playerLocation.getWorld(), playerLocation.getX(), region.getMaximumY() + 1, playerLocation.getZ());
                onlinePlayers.teleport(teleportPlayer);
            }
        }

        try {
            session.setBlocks(region, pattern);
            new LogMe("Mine '" + areaName + "' has just been reset.").Success();
            session.close();
            return true;
        }
        catch (MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean AddBlock(String areaName, String blockName) {
        try {
            GetAreaBlocks().put(areaName, blockName);
            return true;
        }
        catch (NullPointerException | ClassCastException | UnsupportedOperationException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean RemoveBlock(String areaName, String blockName) {
        try {
            String blockList = GetAreaBlocks().get(areaName);
            String newList = blockList.replace(blockName, "");
            GetAreaBlocks().put(areaName, newList);
            return true;
        }
        catch (NullPointerException | ClassCastException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
