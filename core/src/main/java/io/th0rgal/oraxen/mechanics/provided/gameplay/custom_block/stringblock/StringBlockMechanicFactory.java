package io.th0rgal.oraxen.mechanics.provided.gameplay.custom_block.stringblock;

import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.mechanics.MechanicFactory;
import io.th0rgal.oraxen.mechanics.MechanicsManager;
import io.th0rgal.oraxen.mechanics.provided.gameplay.custom_block.stringblock.sapling.SaplingListener;
import io.th0rgal.oraxen.mechanics.provided.gameplay.custom_block.stringblock.sapling.SaplingTask;
import io.th0rgal.oraxen.nms.NMSHandlers;
import io.th0rgal.oraxen.utils.VersionUtil;
import io.th0rgal.oraxen.utils.logs.Logs;
import net.kyori.adventure.key.Key;
import org.apache.commons.lang3.Range;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.blockstate.MultiVariant;
import team.unnamed.creative.blockstate.Variant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringBlockMechanicFactory extends MechanicFactory {

    public static final Map<Integer, StringBlockMechanic> BLOCK_PER_VARIATION = new HashMap<>();
    public static final Integer MAX_BLOCK_VARIATION = 127;
    private final Map<String, MultiVariant> variants = new HashMap<>();
    private static StringBlockMechanicFactory instance;
    public final List<String> toolTypes;
    private boolean sapling;
    private static SaplingTask saplingTask;
    private final int saplingGrowthCheckDelay;
    public final boolean customSounds;
    public final boolean disableVanillaString;

    public StringBlockMechanicFactory(ConfigurationSection section) {
        super(section);
        instance = this;
        variants.put("east=false,west=false,south=false,north=false,attached=false,disarmed=false,powered=false", MultiVariant.of(Variant.builder().model(Key.key("block/barrier")).build()));
        toolTypes = section.getStringList("tool_types");
        saplingGrowthCheckDelay = section.getInt("sapling_growth_check_delay");
        sapling = false;
        customSounds = OraxenPlugin.get().configsManager().getMechanics().getConfigurationSection("custom_block_sounds").getBoolean("stringblock_and_furniture", true);
        disableVanillaString = section.getBoolean("disable_vanilla_strings", true);

        // this modifier should be executed when all the items have been parsed, just
        // before zipping the pack
        BlockState tripwireState = OraxenPlugin.get().packGenerator().resourcePack().blockState(Key.key("minecraft:tripwire"));
        if (tripwireState != null) tripwireState.variants().putAll(variants);
        else tripwireState = BlockState.of(Key.key("minecraft:tripwire"), variants);
        OraxenPlugin.get().packGenerator().resourcePack().blockState(tripwireState);

        MechanicsManager.registerListeners(OraxenPlugin.get(), getMechanicID(), new StringBlockMechanicListener(), new SaplingListener());
        if (customSounds) MechanicsManager.registerListeners(OraxenPlugin.get(), getMechanicID(), new StringBlockSoundListener());

        // Physics-related stuff
        if (VersionUtil.isPaperServer())
            MechanicsManager.registerListeners(OraxenPlugin.get(), getMechanicID(), new StringBlockMechanicListener.StringBlockMechanicPaperListener());
        if (!VersionUtil.isPaperServer() || !NMSHandlers.isTripwireUpdatesDisabled())
            MechanicsManager.registerListeners(OraxenPlugin.get(), getMechanicID(), new StringBlockMechanicListener.StringBlockMechanicPhysicsListener());
        if (VersionUtil.isPaperServer() && VersionUtil.atOrAbove("1.20.1") && !NMSHandlers.isTripwireUpdatesDisabled()) {
            Logs.logError("Papers block-updates.disable-tripwire-updates is not enabled.");
            Logs.logWarning("It is recommended to enable this setting for improved performance and prevent bugs with tripwires");
            Logs.logWarning("Otherwise Oraxen needs to listen to very taxing events, which also introduces some bugs");
            Logs.logWarning("You can enable this setting in ServerFolder/config/paper-global.yml", true);
        }
    }

    public static StringBlockMechanic getBlockMechanic(@NotNull Tripwire blockData) {
        return BLOCK_PER_VARIATION.values().stream().filter(m -> m.blockData().equals(blockData)).findFirst().orElse(null);
    }

    public static boolean isEnabled() {
        return instance != null;
    }

    public static StringBlockMechanicFactory getInstance() {
        return instance;
    }


    /**
     * Attempts to set the block directly to the model and texture of an Oraxen item.
     *
     * @param block  The block to update.
     * @param itemId The Oraxen item ID.
     */
    public static void setBlockModel(Block block, String itemId) {
        final MechanicFactory mechanicFactory = MechanicsManager.getMechanicFactory("stringblock");
        StringBlockMechanic stringBlockMechanic = (StringBlockMechanic) mechanicFactory.getMechanic(itemId);
        block.setBlockData(stringBlockMechanic.blockData());
    }

    @Override
    public StringBlockMechanic getMechanic(String itemID) {
        return (StringBlockMechanic) super.getMechanic(itemID);
    }

    @Override
    public StringBlockMechanic getMechanic(ItemStack itemStack) {
        return (StringBlockMechanic) super.getMechanic(itemStack);
    }

    @Override
    public StringBlockMechanic parse(ConfigurationSection section) {
        StringBlockMechanic mechanic = new StringBlockMechanic(this, section);
        if (!Range.between(1, MAX_BLOCK_VARIATION).contains(mechanic.customVariation())) {
            Logs.logError("The custom variation of the block " + mechanic.getItemID() + " is not between 1 and " + MAX_BLOCK_VARIATION + "!");
            Logs.logWarning("The item has failed to build for now to prevent bugs and issues.");
            return null;
        }

        MultiVariant multiVariant = MultiVariant.of(Variant.builder().model(mechanic.model()).build());
        variants.put(getBlockstateVariantName(mechanic), multiVariant);
        BLOCK_PER_VARIATION.put(mechanic.customVariation(), mechanic);
        addToImplemented(mechanic);
        return mechanic;
    }

    private String getBlockstateVariantName(StringBlockMechanic mechanic) {
        Tripwire t = mechanic.blockData();
        return "east=" + t.hasFace(BlockFace.EAST)
                + ",west=" + t.hasFace(BlockFace.WEST)
                + ",south=" + t.hasFace(BlockFace.SOUTH)
                + ",north=" + t.hasFace(BlockFace.NORTH)
                + ",attached=" + t.isAttached()
                + ",disarmed=" + t.isDisarmed()
                + ",powered=" + t.isPowered();
    }

    public void registerSaplingMechanic() {
        if (sapling) return;
        if (saplingTask != null) saplingTask.cancel();

        // Disabled for abit as OraxenItems.getItems() here
        // Dont register if there is no sapling in configs
//        List<String> saplingList = new ArrayList<>();
//        for (ItemBuilder itemBuilder : OraxenItems.getItems()) {
//            String id = OraxenItems.getIdByItem(itemBuilder.build());
//            StringBlockMechanic mechanic = (StringBlockMechanic) StringBlockMechanicFactory.getInstance().getMechanic(id);
//            if (mechanic == null || !mechanic.isSapling()) continue;
//            saplingList.add(id);
//        }
//        if (saplingList.isEmpty()) return;

        saplingTask = new SaplingTask(saplingGrowthCheckDelay);
        saplingTask.runTaskTimer(OraxenPlugin.get(), 0, saplingGrowthCheckDelay);
        sapling = true;
    }
}
