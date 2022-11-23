package com.minelittlepony.unicopia;

import java.util.HashSet;
import java.util.Set;

import com.minelittlepony.common.util.GamePaths;
import com.minelittlepony.common.util.settings.*;

public class Config extends com.minelittlepony.common.util.settings.Config {
    public final Setting<Set<String>> speciesWhiteList = value("server", "speciesWhiteList", (Set<String>)new HashSet<String>())
            .addComment("A whitelist of races permitted on the server")
            .addComment("Races added to this list can be used by anyone,")
            .addComment("whilst any ones left off are not permitted")
            .addComment("An empty list disables whitelisting entirely.");

    public final Setting<Boolean> enableCheats = value("server", "enableCheats", false)
            .addComment("Allows use of the /race, /disguise, and /gravity commands");

    public final Setting<Boolean> makeCursesTreasure = value("server", "makeCursesTreasure", false)
            .addComment("If true the curses added by unicopia will not appear in the enchanting table");
    
    public final Setting<Boolean> allowAlicorn = value("server", "allowAlicorn", false)
            .addComment("If true alicorns are a vaild choosable race in survival")
            .addComment("May require cheats enabled to allow the /race command to access");

    public final Setting<Integer> batBlindness = value("client", "batBlindness", 3)
            .addComment("Sets how much a bat pony is affected by the sun")
            .addComment("0 - no effect")
            .addComment("1 - whiteout on inital contact")
            .addComment("2 - added ringing noise")
            .addComment("3 - added burning effect (default)");

    public final Setting<Integer> turbulenceLevel = value("client", "turbulenceLevel", 3)
            .addComment("Determines how severe weather pushes flying players around")
            .addComment("0 - none")
            .addComment("1 - quarter default")
            .addComment("2 - half default")
            .addComment("3 - default")
            .addComment("4 - double default");

    public final Setting<Integer> turbulenceFrequency = value("client", "turbulenceFrequency", 3)
            .addComment("Determines how often weather pushes players around")
            .addComment("0 - half default")
            .addComment("1 - default")
            .addComment("2 - double default");

    public final Setting<Race> preferredRace = value("client", "preferredRace", Race.EARTH)
            .addComment("The default preferred race")
            .addComment("This is the race a client requests when first joining a game")
            .addComment("It is the default used both when Mine Little Pony is not installed")
            .addComment("and when they respond with a human race.");

    public final Setting<Boolean> ignoreMineLP = value("client", "ignoreMineLP", false)
            .addComment("If true Mine Little Pony will not be considered when determining the race to use")
            .addComment("The result will always be what is set by this config file.");

    public final Setting<Boolean> disableWaterPlantsFix = value("compatibility", "disableWaterPlantsFix", false)
            .addComment("Disables this mod's built in fix for making sea plants waterlogged")
            .addComment("Turn this ON if you're using another mod that does something similar of if you encounter copatibility issues with other mods.");

    public Config() {
        super(HEIRARCHICAL_JSON_ADAPTER, GamePaths.getConfigDirectory().resolve("unicopia.json"));
    }
}
