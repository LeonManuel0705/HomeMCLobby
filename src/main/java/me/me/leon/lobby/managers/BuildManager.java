package me.leon.lobby.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BuildManager {

    private Set<UUID> builders;

    public BuildManager() {
        this.builders = new HashSet<>();
    }

    public void toggleBuild(UUID uuid) {
        if (builders.contains(uuid)) {
            builders.remove(uuid);
        } else {
            builders.add(uuid);
        }
    }

    public boolean isBuilding(UUID uuid) {
        return builders.contains(uuid);
    }

    public void setBuild(UUID uuid, boolean building) {
        if (building) {
            builders.add(uuid);
        } else {
            builders.remove(uuid);
        }
    }
}
