package io.th0rgal.oraxen.config;

import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.font.FontEvents;
import io.th0rgal.oraxen.nms.GlyphHandlers;
import io.th0rgal.oraxen.pack.server.PackServerType;
import io.th0rgal.oraxen.utils.AdventureUtils;
import io.th0rgal.oraxen.utils.BlockHelpers;
import io.th0rgal.oraxen.utils.VersionUtil;
import io.th0rgal.oraxen.utils.customarmor.CustomArmorType;
import io.th0rgal.oraxen.utils.customarmor.ShaderArmorTextures;
import io.th0rgal.oraxen.utils.logs.Logs;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

public enum Settings {
    // Generic Plugin stuff
    DEBUG("debug", false),
    PLUGIN_LANGUAGE("Plugin.language", "english"),
    KEEP_UP_TO_DATE("Plugin.keep_this_up_to_date", true),
    REPAIR_COMMAND_ORAXEN_DURABILITY("Plugin.commands.repair.oraxen_durability_only", false),
    GENERATE_DEFAULT_ASSETS("Plugin.generation.default_assets", true),
    GENERATE_DEFAULT_CONFIGS("Plugin.generation.default_configs", true),
    FORMAT_INVENTORY_TITLES("Plugin.formatting.inventory_titles", true),
    FORMAT_TITLES("Plugin.formatting.titles", true),
    FORMAT_SUBTITLES("Plugin.formatting.subtitles", true),
    FORMAT_ACTION_BAR("Plugin.formatting.action_bar", true),
    FORMAT_ANVIL("Plugin.formatting.anvil", true),
    FORMAT_SIGNS("Plugin.formatting.signs", true),
    FORMAT_CHAT("Plugin.formatting.chat", true),
    FORMAT_BOOKS("Plugin.formatting.books", true),

    // WorldEdit
    WORLDEDIT_NOTEBLOCKS("WorldEdit.noteblock_mechanic", false),
    WORLDEDIT_STRINGBLOCKS("WorldEdit.stringblock_mechanic", false),
    WORLDEDIT_FURNITURE("WorldEdit.furniture_mechanic", false),

    // Glyphs
    GLYPH_HANDLER("Glyphs.glyph_handler", GlyphHandlers.GlyphHandler.VANILLA),
    SHOW_PERMISSION_EMOJIS("Glyphs.emoji_list_permission_only", true),
    UNICODE_COMPLETIONS("Glyphs.unicode_completions", true),
    GLYPH_HOVER_TEXT("Glyphs.chat_hover_text", "<glyph_placeholder>"),

    // Chat
    CHAT_HANDLER("Chat.chat_handler", VersionUtil.atOrAbove("1.19") && VersionUtil.isPaperServer() ? FontEvents.ChatHandler.MODERN : FontEvents.ChatHandler.LEGACY),

    // Config Tools
    //CONFIGS_VERSION("configs_version", "false"),
    UPDATE_CONFIGS("ConfigsTools.enable_configs_updater", true),
    DISABLE_AUTOMATIC_MODEL_DATA("ConfigsTools.disable_automatic_model_data", false),
    DISABLE_AUTOMATIC_GLYPH_CODE("ConfigsTools.disable_automatic_glyph_code", false),
    SKIPPED_MODEL_DATA_NUMBERS("ConfigsTools.skipped_model_data_numbers", List.of()),
    ERROR_ITEM("ConfigsTools.error_item", Map.of("material", Material.PODZOL, "excludeFromInventory", false, "injectID", false)),

    // Custom Armor
    CUSTOM_ARMOR_TYPE("CustomArmor.type", VersionUtil.atOrAbove("1.20") ? CustomArmorType.TRIMS : CustomArmorType.SHADER),
    DISABLE_LEATHER_REPAIR_CUSTOM("CustomArmor.disable_leather_repair", true),
    CUSTOM_ARMOR_TRIMS_MATERIAL("CustomArmor.trims_settings.material_replacement", "CHAINMAIL"),
    CUSTOM_ARMOR_TRIMS_ASSIGN("CustomArmor.trims_settings.auto_assign_settings", true),
    CUSTOM_ARMOR_SHADER_TYPE("CustomArmor.shader_settings.type", ShaderArmorTextures.ShaderType.FANCY),
    CUSTOM_ARMOR_SHADER_RESOLUTION("CustomArmor.shader_settings.armor_resolution", 16),
    CUSTOM_ARMOR_SHADER_ANIMATED_FRAMERATE("CustomArmor.shader_settings.animated_armor_framerate", 24),
    CUSTOM_ARMOR_SHADER_GENERATE_FILES("CustomArmor.shader_settings.generate_armor_shader_files", true),
    CUSTOM_ARMOR_SHADER_GENERATE_CUSTOM_TEXTURES("CustomArmor.shader_settings.generate_custom_armor_textures", true),
    CUSTOM_ARMOR_SHADER_GENERATE_SHADER_COMPATIBLE_ARMOR("CustomArmor.shader_settings.generate_shader_compatible_armor", true),

    // Custom Blocks
    BLOCK_CORRECTION("CustomBlocks.block_correction", BlockHelpers.BlockCorrection.NMS),
    LEGACY_NOTEBLOCKS("CustomBlocks.use_legacy_noteblocks", VersionUtil.atOrAbove("1.20")),
    LEGACY_STRINGBLOCKS("CustomBlocks.use_legacy_stringblocks", false),

    // ItemUpdater
    UPDATE_ITEMS("ItemUpdater.update_items", true),
    UPDATE_ITEMS_ON_RELOAD("ItemUpdater.update_items_on_reload", true),
    OVERRIDE_RENAMED_ITEMS("ItemUpdater.override_renamed_items", false),
    OVERRIDE_ITEM_LORE("ItemUpdater.override_item_lore", false),

    // FurnitureUpdater
    UPDATE_FURNITURE("FurnitureUpdater.update_furniture", true),
    UPDATE_FURNITURE_ON_RELOAD("FurnitureUpdater.update_on_reload", false),
    UPDATE_FURNITURE_ON_LOAD("FurnitureUpdater.update_on_load", false),
    EXPERIMENTAL_FURNITURE_TYPE_UPDATE("FurnitureUpdater.experimental_furniture_type_update", false),
    EXPERIMENTAL_FIX_BROKEN_FURNITURE("FurnitureUpdater.experimental_fix_broken_furniture", false),

    //Misc
    RESET_RECIPES("Misc.reset_recipes", true),
    ADD_RECIPES_TO_BOOK("Misc.add_recipes_to_book", true),
    HIDE_SCOREBOARD_NUMBERS("Misc.hide_scoreboard_numbers", false),
    HIDE_SCOREBOARD_BACKGROUND("Misc.hide_scoreboard_background", false),

    //Pack
    GENERATE("Pack.generation.generate", true),
    EXCLUDED_FILE_EXTENSIONS("Pack.generation.excluded_file_extensions", List.of(".zip")),
    FIX_FORCE_UNICODE_GLYPHS("Pack.generation.fix_force_unicode_glyphs", true),
    VERIFY_PACK_FILES("Pack.generation.verify_pack_files", true),
    GENERATE_MODEL_BASED_ON_TEXTURE_PATH("Pack.generation.auto_generated_models_follow_texture_path", false),
    COMPRESSION("Pack.generation.compression", Deflater.BEST_COMPRESSION),
    PROTECTION("Pack.generation.protection", true),
    COMMENT("Pack.generation.comment", """
            The content of this resourcepack
            belongs to the owner of the Oraxen
            plugin and any complete or partial
            use must comply with the terms and
            conditions of Oraxen
            """.trim()),

    PACK_SERVER_TYPE("Pack.server.type", PackServerType.SELFHOST),
    SELFHOST_PACK_SERVER_PORT("Pack.server.selfhost.port", 8082),
    POLYMATH_SERVER("Pack.server.polymath.server", "atlas.oraxen.com"),
    POLYMATH_SECRET("Pack.server.polymath.secret", "oraxen"),

    SEND_PACK("Pack.dispatch.send_pack", true),
    SEND_ON_RELOAD("Pack.dispatch.send_on_reload", true),
    SEND_PACK_DELAY("Pack.dispatch.delay", -1),
    SEND_PACK_MANDATORY("Pack.dispatch.mandatory", true),
    SEND_PACK_PROMPT("Pack.dispatch.prompt", "<#fa4943>Accept the pack to enjoy a full <b><gradient:#9055FF:#13E2DA>Oraxen</b><#fa4943> experience"),
    SEND_JOIN_MESSAGE("Pack.dispatch.join_message.enabled", false),
    JOIN_MESSAGE_DELAY("Pack.dispatch.join_message.delay", -1),

    //RECEIVE_ENABLED("Pack.receive.enabled", true),
    //RECEIVE_ALLOWED_ACTIONS("Pack.receive.accepted.actions", new SoundAction()),
    //RECEIVE_LOADED_ACTIONS("Pack.receive.loaded.actions"),
    //RECEIVE_FAILED_ACTIONS("Pack.receive.failed_download.actions"),
    //RECEIVE_DENIED_ACTIONS("Pack.receive.denied.actions"),
    //RECEIVE_FAILED_RELOAD_ACTIONS("Pack.receive.failed_reload.actions"),
    //RECEIVE_DOWNLOADED_ACTIONS("Pack.receive.downloaded.actions"),
    //RECEIVE_INVALID_URL_ACTIONS("Pack.receive.invalid_url.actions"),
    //RECEIVE_DISCARDED_ACTIONS("Pack.receive.discarded.actions"),


    // Inventory
    ORAXEN_INV_LAYOUT("oraxen_inventory.menu_layout", Map.of(
            "armors", Map.of("slot", 1, "icon", "emerald_chestplate", "displayname", "<green>Armors", "title", "<main_menu_title><#362753><glyph:menu_items_overlay:colorable>"),
            "blocks", Map.of("slot", 2, "icon", "orax_ore", "displayname", "<green>Blocks", "title", "<main_menu_title><#EDCDEB><glyph:menu_items_overlay:colorable>"),
            "furniture", Map.of("slot", 3, "icon", "chair", "displayname", "<green>Furniture", "title", "<main_menu_title><#F2F2F2><glyph:menu_items_overlay:colorable>"),
            "flowers", Map.of("slot", 4, "icon", "dailily", "displayname", "<green>Flowers", "title", "<main_menu_title><#bf332c><glyph:menu_items_overlay:colorable>"),
            "hats", Map.of("slot", 5, "icon", "crown", "displayname", "<green>Hats", "title", "<main_menu_title><#81B125><glyph:menu_items_overlay:colorable>"),
            "items", Map.of("slot", 6, "icon", "ruby", "displayname", "<green>Items", "title", "<main_menu_title><#DA2E45><glyph:menu_items_overlay:colorable>"),
            "mystical", Map.of("slot", 7, "icon", "legendary_hammer", "displayname", "<green>Mystical", "title", "<main_menu_title><#9AB2E4><glyph:menu_items_overlay:colorable>"),
            "plants", Map.of("slot", 8, "icon", "weed_leaf", "displayname", "<green>Plants", "title", "<main_menu_title><#44C886><glyph:menu_items_overlay:colorable>"),
            "tools", Map.of("slot", 9, "icon", "iron_serpe", "displayname", "<green>Tools", "title", "<main_menu_title><#FFFFFF><glyph:menu_items_overlay:colorable>"),
            "weapons", Map.of("slot", 10, "icon", "energy_crystal_sword", "displayname", "<green>Weapons", "title", "<main_menu_title><#2FB6FF><glyph:menu_items_overlay:colorable>")

    )),
    ORAXEN_INV_ROWS("oraxen_inventory.menu_rows", 6),
    ORAXEN_INV_TITLE("oraxen_inventory.main_menu_title",  "<shift:-18><glyph:menu_items><shift:-193>"),
    ORAXEN_INV_TYPE("oraxen_inventory.main_menu_type", "PAGINATED"),
    ORAXEN_INV_NEXT_ICON("oraxen_inventory.next_page_icon"),
    ORAXEN_INV_PREVIOUS_ICON("oraxen_inventory.previous_page_icon"),
    ORAXEN_INV_EXIT("oraxen_inventory.exit_icon");

    private final String path;
    private final Object defaultValue;

    Settings(String path) {
        this.path = path;
        this.defaultValue = null;
    }

    Settings(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath() {
        return path;
    }
    public Object defaultValue() {
        return defaultValue;
    }

    public Object getValue() {
        return OraxenPlugin.get().configsManager().getSettings().get(path);
    }
    public void setValue(Object value) { setValue(value, true); }
    public void setValue(Object value, boolean save) {
        YamlConfiguration settingFile = OraxenPlugin.get().configsManager().getSettings();
        settingFile.set(path, value);
        try {
            if (save) settingFile.save(OraxenPlugin.get().getDataFolder().toPath().resolve("settings.yml").toFile());
        } catch (Exception e) {
            Logs.logError("Failed to apply changes to settings.yml");
        }
    }

    public String toString(String optionalDefault) {
        return getValue() == null ? optionalDefault : (String) getValue();
    }

    @Override
    public String toString() {
        return (String) getValue();
    }

    public Component toComponent() {
        return AdventureUtils.MINI_MESSAGE.deserialize(getValue().toString());
    }

    public Integer toInt() {
        return toInt(-1);
    }

    /**
     * @param optionalDefault value to return if the path is not an integer
     * @return the value of the path as an int, or the default value if the path is not found
     */
    public Integer toInt(int optionalDefault) {
        try {
            return Integer.parseInt(getValue().toString());
        } catch (NumberFormatException | NullPointerException e) {
            return optionalDefault;
        }
    }

    public Boolean toBool(boolean defaultValue) {
        return getValue() == null ? defaultValue : (Boolean) getValue();
    }

    public Boolean toBool() {
        return (Boolean) getValue();
    }

    public List<String> toStringList() {
        return OraxenPlugin.get().configsManager().getSettings().getStringList(path);
    }

    public ConfigurationSection toConfigSection() {
        return OraxenPlugin.get().configsManager().getSettings().getConfigurationSection(path);
    }

}