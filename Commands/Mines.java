public class Mines implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            new LogMe("Commands need to be executed from a player.").Error();
            return true;
        }

        Player ply = (Player) sender;

        if (args.length <= 0) {
            new Chat(ply, ChatColor.RED + "Unable to execute this command.").WatermarkMessage();
            return true;
        }

        String areaCommand = args[0];

        if (args[1] == null) {
            if (areaCommand.equals("reload")) {
                if (!ply.hasPermission(Main.GetInstance().config.getItem("hylumina.permissions.reload").toString())) {
                    new Chat(ply, ChatColor.RED + "You don't have permission to do this.").WatermarkMessage();
                    return true;
                }

                Main.GetInstance().config.reload();
                Main.GetInstance().mines.reload();
                new Chat(ply, ChatColor.GREEN + "Config reloaded.").WatermarkMessage();
                return true;
            }

            new Chat(ply, ChatColor.RED + "Area name cannot be null.").WatermarkMessage();
            return true;
        }

        String areaName = args[1];

        switch (areaCommand) {
            case "create":
                if (!ply.hasPermission(Main.GetInstance().config.getItem("hylumina.permissions.create").toString())) {
                    new Chat(ply, ChatColor.RED + "You don't have permission to do this.").WatermarkMessage();
                    return true;
                }

                if (!new Area(areaName, ply, ply.getWorld().getName()).CreateArea()) {
                    new Chat(ply, ChatColor.RED + "Unable to create the area '" + areaName + "'.").WatermarkMessage();
                    return true;
                }

                new Chat(ply, ChatColor.GREEN + "The area '" + areaName +"' has been created.").WatermarkMessage();
                break;
            case "delete":
                if (!ply.hasPermission(Main.GetInstance().config.getItem("hylumina.permissions.delete").toString())) {
                    new Chat(ply, ChatColor.RED + "You don't have permission to do this.").WatermarkMessage();
                    return true;
                }

                if (!new Area(areaName).RemoveArea()) {
                    new Chat(ply, ChatColor.RED + "Unable to delete the area '" + areaName + "'.").WatermarkMessage();
                    return true;
                }

                new Chat(ply, ChatColor.GREEN + "The area '" + areaName + "' has been deleted.").WatermarkMessage();
                break;
            case "reset":
                if (!ply.hasPermission(Main.GetInstance().config.getItem("hylumina.permissions.reset").toString())) {
                    new Chat(ply, ChatColor.RED + "You don't have permission to do this.").WatermarkMessage();
                    return true;
                }

                if (!new Area(areaName).ResetArea()) {
                    new Chat(ply, ChatColor.RED + "Unable to reset the area '" + areaName + "'.").WatermarkMessage();
                    return true;
                }

                new Chat(ply, ChatColor.GREEN + "The area '" + areaName + "' has been reset.").WatermarkMessage();
                break;
            case "add":
                if (!ply.hasPermission(Main.GetInstance().config.getItem("hylumina.permissions.add-material").toString())) {
                    new Chat(ply, ChatColor.RED + "You don't have permission to do this.").WatermarkMessage();
                    return true;
                }

                String blockName = args[2];

                if (blockName == null) {
                    new Chat(ply, ChatColor.RED + "Material name cannot be null.").WatermarkMessage();
                    return true;
                }

                if (Material.getMaterial(blockName) == null) {
                    new Chat(ply, ChatColor.RED + blockName + " is not a valid material.").WatermarkMessage();
                    return true;
                }

                if (!new Area(areaName, blockName).AddBlock()) {
                    new Chat(ply, ChatColor.RED + "Unable to add that material to the area '" + areaName + "'.").WatermarkMessage();
                    return true;
                }

                new Chat(ply, ChatColor.GREEN + "The material '" + blockName + "' has been added to the area.").WatermarkMessage();
                break;
            case "remove":
                if (!ply.hasPermission(Main.GetInstance().config.getItem("hylumina.permissions.remove-material").toString())) {
                    new Chat(ply, ChatColor.RED + "You don't have permission to do this.").WatermarkMessage();
                    return true;
                }

                String blockNameRemove = args[2];

                if (blockNameRemove == null) {
                    new Chat(ply, ChatColor.RED + "Material name cannot be null.").WatermarkMessage();
                    return true;
                }

                if (Material.getMaterial(blockNameRemove) == null) {
                    new Chat(ply, ChatColor.RED + blockNameRemove + " is not a valid material.").WatermarkMessage();
                    return true;
                }

                if (!new Area(areaName, blockNameRemove).RemoveBlock()) {
                    new Chat(ply, ChatColor.RED + "Unable to remove that material from the area '" + areaName + "'.").WatermarkMessage();
                    return true;
                }

                new Chat(ply, ChatColor.GREEN + "The material '" + blockNameRemove + "' has been removed from the area.").WatermarkMessage();
                break;
            default:
                new Chat(ply, ChatColor.RED + "Unable to execute this command.").WatermarkMessage();
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("hm")) {
            String subCommandName = args[0];
            List<String> itemList = new ArrayList<>();

            if (args.length == 1) {
                itemList.add("create");
                itemList.add("delete");
                itemList.add("reset");
                itemList.add("add");
                itemList.add("remove");
                itemList.add("reload");
            }

            List<String> subItemList = new ArrayList<>();
            switch (subCommandName) {
                case "create":
                case "delete":
                case "reset":
                    if (args.length == 2) {
                        if (!Area.GetAreas().isEmpty()) {
                            subItemList.addAll(Area.GetAreas());
                        }
                        else {
                            subItemList.add("areaName");
                        }
                    }
                    break;
                case "add":
                case "remove":
                    if (args.length == 2) {
                        subItemList.add("areaName");
                    }
                    else if (args.length == 3) {
                        for (Material material : Material.values()) {
                            subItemList.add(material.name());
                        }
                    }
                    break;
            }
        }
        return null;
    }
}
