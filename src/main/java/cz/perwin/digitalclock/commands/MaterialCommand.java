package cz.perwin.digitalclock.commands;

import org.bukkit.Material;

public class MaterialCommand {
	protected boolean isUsableNumber(String s) {
		if(s.matches("^[0-9]*([,]{1}[0-9]{0,2}){0,1}$")) {
			return true;
		}
		return false;
	}

	/*
	protected boolean isPermitted(Player player, String[] args) {
		String command = args[0];
		String arg = args[2].contains(":") ? args[2].substring(0, args[2].indexOf(':')) : args[2];
		if(player.hasPermission("digitalclock." + command + ".*") || player.hasPermission("digitalclock." + command + "." + arg) || player.isOp()) {
			return true;
		}
		return false;

	}
	*/

	@SuppressWarnings("deprecation")
	/* Copied from original: https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/Material.java */
	public boolean isSolid(Material m) {
		if(!m.isBlock() && m.getId() != 0) {
			return false;
		}
		switch(m) {
		case AIR: //+
		case STONE:
		case GRASS:
		case DIRT:
		case COBBLESTONE:
		case WOOD:
		case BEDROCK:
		case SAND:
		case GRAVEL:
		case GOLD_ORE:
		case IRON_ORE:
		case COAL_ORE:
		case LOG:
		case LEAVES:
		case SPONGE:
		case GLASS:
		case LAPIS_ORE:
		case LAPIS_BLOCK:
		case DISPENSER:
		case SANDSTONE:
		case NOTE_BLOCK:
		case BED_BLOCK:
		case PISTON_STICKY_BASE:
		case PISTON_BASE:
		case PISTON_EXTENSION:
		case WOOL:
		case PISTON_MOVING_PIECE:
		case GOLD_BLOCK:
		case IRON_BLOCK:
		case DOUBLE_STEP:
		case STEP:
		case BRICK:
		case TNT:
		case BOOKSHELF:
		case MOSSY_COBBLESTONE:
		case OBSIDIAN:
		case MOB_SPAWNER:
		case WOOD_STAIRS:
		case CHEST:
		case DIAMOND_ORE:
		case DIAMOND_BLOCK:
		case WORKBENCH:
		case SOIL:
		case FURNACE:
		case BURNING_FURNACE:
		//case SIGN_POST:
		//case WOODEN_DOOR:
		case COBBLESTONE_STAIRS:
		//case WALL_SIGN:
		case STONE_PLATE:
		case IRON_DOOR_BLOCK:
		case WOOD_PLATE:
		case REDSTONE_ORE:
		case GLOWING_REDSTONE_ORE:
		case ICE:
		case SNOW_BLOCK:
		case CACTUS:
		case CLAY:
		case JUKEBOX:
		case FENCE:
		case PUMPKIN:
		case NETHERRACK:
		case SOUL_SAND:
		case GLOWSTONE:
		case JACK_O_LANTERN:
		//case CAKE_BLOCK:
		//case LOCKED_CHEST:
		case STAINED_GLASS:
		//case TRAP_DOOR:
		//case MONSTER_EGGS:
		case SMOOTH_BRICK:
		case HUGE_MUSHROOM_1:
		case HUGE_MUSHROOM_2:
		case IRON_FENCE:
		case THIN_GLASS:
		case MELON_BLOCK:
		//case FENCE_GATE:
		case BRICK_STAIRS:
		case SMOOTH_STAIRS:
		case MYCEL:
		case NETHER_BRICK:
		case NETHER_FENCE:
		case NETHER_BRICK_STAIRS:
		//case ENCHANTMENT_TABLE:
		//case BREWING_STAND:
		//case CAULDRON:
		//case ENDER_PORTAL_FRAME:
		case ENDER_STONE:
		case DRAGON_EGG:
		//case REDSTONE_LAMP_OFF:
		//case REDSTONE_LAMP_ON:
		case WOOD_DOUBLE_STEP:
		case WOOD_STEP:
		case SANDSTONE_STAIRS:
		case EMERALD_ORE:
		//case ENDER_CHEST:
		case EMERALD_BLOCK:
		case SPRUCE_WOOD_STAIRS:
		case BIRCH_WOOD_STAIRS:
		case JUNGLE_WOOD_STAIRS:
		case COMMAND:
		case BEACON:
		case COBBLE_WALL:
		//case ANVIL:
		case TRAPPED_CHEST:
		case GOLD_PLATE:
		case IRON_PLATE:
		case DAYLIGHT_DETECTOR:
		case REDSTONE_BLOCK:
		case QUARTZ_ORE:
		case HOPPER:
		case QUARTZ_BLOCK:
		case QUARTZ_STAIRS:
		case DROPPER:
		case STAINED_CLAY:
		case HAY_BLOCK:
		case HARD_CLAY:
		case COAL_BLOCK:
		case STAINED_GLASS_PANE:
		case LEAVES_2:
		case LOG_2:
		case ACACIA_STAIRS:
		case DARK_OAK_STAIRS:
		case PACKED_ICE:
			return true;
		default:
			return false;
		}
	}
}
