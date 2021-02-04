package me.finn.kitpvp.kit;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.utils.Colorize;

public class KitManager {

    private KitPVP pl;

    public KitManager(KitPVP pl) {
        this.pl = pl;
    }

    public Kit getKit(String name, Boolean ignoreCase) {
        if (ignoreCase) {
            for (Kit kit : pl.kits) {
                if (kit.getName().equalsIgnoreCase(name) || kit.getDisplayName().equalsIgnoreCase(name)) {
                    return kit;
                }
            }
        } else {
            for (Kit kit : pl.kits) {
                if (kit.getName().equals(name) || kit.getDisplayName().equals(name)) {
                    return kit;
                }
            }
        }
        return null;
    }

    public Kit newKit(String displayName) {
        Kit kit = new Kit();
        kit.setDisplayName(displayName);
        kit.setName(Colorize.removeColorNotation(displayName)
                .toLowerCase()
                .replace(" ", "_"));
        return kit;
    }

    public void deleteKit(Kit kit) {
        pl.kits.remove(kit);
        pl.cm.deleteKit(kit);
    }

}
