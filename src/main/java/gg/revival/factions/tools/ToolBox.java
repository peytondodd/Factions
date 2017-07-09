package gg.revival.factions.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ToolBox {

    public static ItemStack getClaimingStick() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();

        //TODO: Get stick displayname from config
        //TODO: Get stick lore from config

        stick.setItemMeta(stickMeta);

        return stick;
    }

    public static WorldLocations getLocationEnum(Location location) {
        if (isWilderness(location)) {
            return WorldLocations.WILDERNESS;
        } else if (isWarzone(location)) {
            return WorldLocations.WARZONE;
        } else if (isNetherWarzone(location)) {
            return WorldLocations.NETHER_WARZONE;
        } else if (isNether(location)) {
            return WorldLocations.NETHER;
        } else if (isEnd(location)) {
            return WorldLocations.END;
        }

        return null;
    }

    public static boolean isNether(Location location) {
        if (location.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            return true;
        }

        return false;
    }

    public static boolean isEnd(Location location) {
        if (location.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            return true;
        }

        return false;
    }

    public static boolean isOverworld(Location location) {
        if (!isNether(location) && !isEnd(location)) {
            return true;
        }

        return false;
    }

    public static boolean isWarzone(Location location) {
        if (isOverworld(location)) {
            double x = location.getX();
            double z = location.getZ();

            if (x <= Configuration.WARZONE_RADIUS && z <= Configuration.WARZONE_RADIUS) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNonBuildableWarzone(Location location) {
        if(isWarzone(location)) {
            double x = location.getX();
            double z = location.getZ();

            if(x <= Configuration.BUILDABLE_WARZONE_RADIUS && z <= Configuration.BUILDABLE_WARZONE_RADIUS) {
                return true;
            }
        }

        return false;
    }

    public static boolean isWilderness(Location location) {
        if (!isWarzone(location) && isOverworld(location)) {
            return true;
        }

        return false;
    }

    public static boolean isNetherWarzone(Location location) {
        if (isNether(location)) {
            double x = location.getX();
            double z = location.getZ();

            if (x <= Configuration.NETHER_WARZONE_RADIUS && z <= Configuration.NETHER_WARZONE_RADIUS) {
                return true;
            }
        }

        return false;
    }

    public enum WorldLocations {
        WILDERNESS, WARZONE, NETHER, NETHER_WARZONE, END
    }

    public static boolean overlapsWarzone(double x1, double x2, double z1, double z2) {
        double[] vals = new double[2];

        double min = Math.min(Configuration.WARZONE_RADIUS, -Configuration.WARZONE_RADIUS);
        double max = Math.max(Configuration.WARZONE_RADIUS, -Configuration.WARZONE_RADIUS);

        vals[0] = x1;
        vals[1] = x2;

        Arrays.sort(vals);

        if(min > vals[1] || max < vals[0])
            return false;

        vals[0] = z1;
        vals[1] = z2;

        Arrays.sort(vals);

        if(min > vals[1] || max < vals[0])
            return false;

        return true;
    }
}
