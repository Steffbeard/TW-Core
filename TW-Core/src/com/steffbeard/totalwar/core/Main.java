package com.steffbeard.totalwar.core;

import com.steffbeard.totalwar.core.utils.KeyUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.Location;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginManager;
import java.io.File;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import com.steffbeard.totalwar.core.listeners.HopperListener;
import com.steffbeard.totalwar.core.listeners.BunchOfKeysListener;
import com.steffbeard.totalwar.core.listeners.BlocksListener;
import com.steffbeard.totalwar.core.listeners.GlobalListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.steffbeard.totalwar.core.listeners.ArrowListener;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin
{
    public static Main instance;
    private int timer;
    protected ItemStack key;
    protected ItemStack masterKey;
    protected ItemStack keyClone;
    protected ItemStack bunchOfKeys;
    protected ItemStack padlockFinder;
    protected Config config;
    protected Messages messages;
    protected Data data;
	private KeyAPI api;
	
	private Random prng_ = new Random();
    
    public void onEnable() {
        final File dataFolder = this.getDataFolder();
        this.config = new Config(dataFolder);
        try {
            this.config.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.messages = new Messages(dataFolder);
        try {
            this.messages.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.data = new Data(dataFolder);
        try {
            this.data.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.handleLocations();
        final PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents((Listener)new ArrowListener(), (Plugin)this);
        manager.registerEvents((Listener)new GlobalListener(), (Plugin)this);
        manager.registerEvents((Listener)new BlocksListener(), (Plugin)this);
        manager.registerEvents((Listener)new BunchOfKeysListener(), (Plugin)this);
        if (this.config.disableHoppers) {
            manager.registerEvents((Listener)new HopperListener(), (Plugin)this);
            //***********
            //* CRAFTING
            //*	RECIPES
            //***********
            //
            // chain helmet
            //
            final ItemStack chelmet = new ItemStack(Material.CHAINMAIL_HELMET);
            final ItemMeta chmeta = chelmet.getItemMeta();
            chmeta.setDisplayName(ChatColor.GRAY + "Mail coif");
            chmeta.setLore((List<String>)Arrays.asList("'Tis but a scratch.'", "'A scratch!? Your arm's off!'"));
            chelmet.setItemMeta(chmeta);
            chelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            chelmet.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
            final NamespacedKey chkey = new NamespacedKey((Plugin)this, "chainmail_helmet");
            final ShapedRecipe chrecipe = new ShapedRecipe(chkey, chelmet);
            chrecipe.shape(new String[] { "@@@", "@#@" });
            chrecipe.setIngredient('@', Material.IRON_NUGGET);
            chrecipe.setIngredient('#', Material.AIR);
            Bukkit.addRecipe((Recipe)chrecipe);
            //
            // chainmail chest
            //
            final ItemStack cchestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
            final ItemMeta ccmeta = cchestplate.getItemMeta();
            ccmeta.setDisplayName(ChatColor.GRAY + "Mail Tunic");
            ccmeta.setLore((List<String>)Arrays.asList("'Very airy'"));
            cchestplate.setItemMeta(ccmeta);
            cchestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            final NamespacedKey cckey = new NamespacedKey((Plugin)this, "chainmail_chestplate");
            final ShapedRecipe ccrecipe = new ShapedRecipe(cckey, cchestplate);
            ccrecipe.shape(new String[] { "@#@", "@@@", "@@@" });
            ccrecipe.setIngredient('@', Material.IRON_NUGGET);
            ccrecipe.setIngredient('#', Material.AIR);
            Bukkit.addRecipe((Recipe)ccrecipe);
            //
            // chainmail leggings
            //
            final ItemStack cleggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
            final ItemMeta clmeta = cleggings.getItemMeta();
            clmeta.setDisplayName(ChatColor.GRAY + "Mail Leggings");
            clmeta.setLore((List<String>)Arrays.asList("'Stylish.'"));
            cleggings.setItemMeta(clmeta);
            final NamespacedKey clkey = new NamespacedKey((Plugin)this, "chainmail_leggings");
            final ShapedRecipe clrecipe = new ShapedRecipe(clkey, cleggings);
            clrecipe.shape(new String[] { "@@@", "@#@", "@#@" });
            clrecipe.setIngredient('@', Material.IRON_NUGGET);
            clrecipe.setIngredient('#', Material.AIR);
            Bukkit.addRecipe((Recipe)clrecipe);
            //
            // chain boots
            //
            final ItemStack cboots = new ItemStack(Material.CHAINMAIL_BOOTS);
            final ItemMeta cbmeta = cboots.getItemMeta();
            cbmeta.setDisplayName(ChatColor.GRAY + "Mail Boots");
            cbmeta.setLore((List<String>)Arrays.asList("'Stylish.'"));
            cboots.setItemMeta(cbmeta);
            final NamespacedKey cbkey = new NamespacedKey((Plugin)this, "chainmail_boots");
            final ShapedRecipe cbrecipe = new ShapedRecipe(cbkey, cboots);
            cbrecipe.shape(new String[] { "@#@", "@#@" });
            cbrecipe.setIngredient('@', Material.IRON_NUGGET);
            cbrecipe.setIngredient('#', Material.AIR);
            Bukkit.addRecipe((Recipe)cbrecipe);
            //
            // saddle
            //
            final ItemStack saddle = new ItemStack(Material.SADDLE, 1);
            final ItemMeta smeta = saddle.getItemMeta();
            smeta.setDisplayName(ChatColor.GRAY + "Saddle");
            saddle.setItemMeta(smeta);
            final NamespacedKey skey = new NamespacedKey((Plugin)this, "saddle");
            final ShapedRecipe srecipe = new ShapedRecipe(skey, saddle);
            srecipe.shape(new String[] { "LIL", "LLL" });
            srecipe.setIngredient('L', Material.LEATHER);
            srecipe.setIngredient('I', Material.IRON_INGOT);
            Bukkit.addRecipe((Recipe)srecipe);
            //
            // iron horse armor
            //
            final ItemStack ironhorsearmor = new ItemStack(Material.IRON_BARDING);
            final ItemMeta ihameta = ironhorsearmor.getItemMeta();
            ihameta.setDisplayName(ChatColor.GRAY + "Horse Barding");
            ironhorsearmor.setItemMeta(ihameta);
            final NamespacedKey ihakey = new NamespacedKey((Plugin)this, "iron_horse_armor");
            final ShapedRecipe iharecipe = new ShapedRecipe(ihakey, ironhorsearmor);
            iharecipe.shape(new String[] { "I##", "ISI", "III" });
            iharecipe.setIngredient('I', Material.IRON_INGOT);
            iharecipe.setIngredient('S', Material.SADDLE);
            iharecipe.setIngredient('#', Material.AIR);
            Bukkit.addRecipe((Recipe)iharecipe);
            //
            // gold horse armor
            //
            final ItemStack goldhorsearmor = new ItemStack(Material.GOLD_BARDING);
            final ItemMeta ghameta = goldhorsearmor.getItemMeta();
            ghameta.setDisplayName(ChatColor.RED + "Horse Barding");
            goldhorsearmor.setItemMeta(ghameta);
            final NamespacedKey ghakey = new NamespacedKey((Plugin)this, "gold_horse_armor");
            final ShapedRecipe gharecipe = new ShapedRecipe(ghakey, goldhorsearmor);
            gharecipe.shape(new String[] { "I##", "ISI", "III" });
            gharecipe.setIngredient('I', Material.GOLD_INGOT);
            gharecipe.setIngredient('S', Material.SADDLE);
            gharecipe.setIngredient('#', Material.AIR);
            Bukkit.addRecipe((Recipe)gharecipe);
            //
            // Salt
            //
            final ItemStack salt = new ItemStack(Material.SUGAR);
            final ItemMeta saltmeta = salt.getItemMeta();
            saltmeta.setDisplayName(new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append("Salt").toString());
            salt.setItemMeta(saltmeta);
            Bukkit.addRecipe((Recipe)new FurnaceRecipe(salt, Material.WATER_BUCKET));
            //
            // key
            //
            final ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
            final ItemMeta keymeta = key.getItemMeta();
            keymeta.setDisplayName(ChatColor.GOLD + "Key");
            key.setItemMeta(keymeta);
            final NamespacedKey keykey = new NamespacedKey((Plugin)this, "key");
            final ShapedRecipe keyrecipe = new ShapedRecipe(keykey, key);
            keyrecipe.shape(new String[] { "I", "L" });
            keyrecipe.setIngredient('I', Material.IRON_INGOT);
            keyrecipe.setIngredient('L', Material.LEVER);
            Bukkit.addRecipe((Recipe)keyrecipe);
            //
            // masterkey
            // 
            final ItemStack masterKey = new ItemStack(Material.NAME_TAG);
            final ItemMeta masterKeymeta = masterKey.getItemMeta();
            masterKeymeta.setDisplayName(ChatColor.DARK_PURPLE + "Master Key");
            masterKey.setItemMeta(masterKeymeta);
            final NamespacedKey masterKeykey = new NamespacedKey((Plugin)this, "master_key");
            final ShapedRecipe masterKeyrecipe = new ShapedRecipe(masterKeykey, masterKey);
            masterKeyrecipe.shape(new String[] { "C", "L" });
            masterKeyrecipe.setIngredient('C', Material.COMMAND);
            masterKeyrecipe.setIngredient('L', Material.LEVER);
            Bukkit.addRecipe((Recipe)masterKeyrecipe);
            //
            // bunch of keys
            //
            final ItemStack bunchOfKeys = new ItemStack(Material.NAME_TAG);
            final ItemMeta bunchOfKeysmeta = bunchOfKeys.getItemMeta();
            bunchOfKeysmeta.setDisplayName(ChatColor.BLUE + "Bunch of keys");
            bunchOfKeys.setItemMeta(bunchOfKeysmeta);
            final NamespacedKey bunchOfKeyskey = new NamespacedKey((Plugin)this, "bunch_of_keys");
            final ShapedRecipe bunchOfKeysrecipe = new ShapedRecipe(bunchOfKeyskey, bunchOfKeys);
            bunchOfKeysrecipe.shape(new String[] { "#S#", "SLS", "#S#" });
            bunchOfKeysrecipe.setIngredient('S', Material.STRING);
            bunchOfKeysrecipe.setIngredient('L', Material.LEVER);
            bunchOfKeysrecipe.setIngredient('#', Material.AIR);
            Bukkit.addRecipe((Recipe)bunchOfKeysrecipe);
            this.getLogger().info("> TOTAL WAR CORE IS ONLINE.");
        }
    }
    
    public void createConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
    }
    
    public void updateTimer() {
        if (!this.getConfig().getBoolean("entityFireTrail")) {
            Bukkit.getScheduler().cancelTask(this.timer);
        }
        else if (!Bukkit.getScheduler().isCurrentlyRunning(this.timer)) {
            this.trackEntity();
        }
    }
    
    // tracks arrows to apply fire when land
    
    private void trackEntity() {
        if (this.getConfig().getBoolean("entityFireTrail")) {
            this.timer = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    for (final Player p : Bukkit.getOnlinePlayers()) {
                        if (Main.this.getConfig().getStringList("EnabledWorlds").contains(p.getWorld().getName())) {
                            for (final Entity e : p.getWorld().getLivingEntities()) {
                                if (!(e instanceof Arrow) && e.getFireTicks() > 0) {
                                    final Block block = e.getLocation().getBlock();
                                    if (block.getType() != Material.AIR) {
                                        continue;
                                    }
                                    block.setType(Material.FIRE);
                                }
                            }
                        }
                    }
                }
            }, 0L, 12L);
        }
    }
    
    // Reduce registered PlayerInteractEvent count. onPlayerInteractAll handles
    //  cancelled events.
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
      if (!event.isCancelled()) {
        onEnchantingTableUse(event);
      }
    }

    @EventHandler(priority = EventPriority.LOWEST) // ignoreCancelled=false
    public void onPlayerInteractAll(PlayerInteractEvent event) {
    }
    
    // buffing bows
    
    @EventHandler
    public void onEntityShootBowEventAlreadyIntializedSoIMadeThisUniqueName(EntityShootBowEvent event) {
      Integer power = event.getBow().getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
      MetadataValue metadata = new FixedMetadataValue(this, power);
      event.getProjectile().setMetadata("power", metadata);
    }

    @EventHandler
    public void onArrowHitEntity(EntityDamageByEntityEvent event) {
      Double multiplier = config.bowBuff;
      if(multiplier <= 1.000001 && multiplier >= 0.999999) {
        return;
      }

      if (event.getEntity() instanceof LivingEntity) {
        Entity damager = event.getDamager();
        if (damager instanceof Arrow) {
          Arrow arrow = (Arrow) event.getDamager();
          Double damage = event.getDamage() * config.bowBuff;
          Integer power = 0;
          if(arrow.hasMetadata("power")) {
            power = arrow.getMetadata("power").get(0).asInt();
          }
          damage *= Math.pow(1.25, power - 5); // f(x) = 1.25^(x - 5)
          event.setDamage(damage);
        }
      }
    }
    
    // Quartz from Gravel

    @EventHandler(ignoreCancelled=true, priority = EventPriority.HIGHEST)
    public void onGravelBreak(BlockBreakEvent e) {
      if(e.getBlock().getType() != Material.GRAVEL
          || config.quartz_gravel_percentage <= 0) {
        return;
      }

      if(prng_.nextInt(100) < config.quartz_gravel_percentage)
      {
        e.setCancelled(true);
        e.getBlock().setType(Material.AIR);
        dropItemAtLocation(e.getBlock().getLocation(), new ItemStack(Material.QUARTZ, 1));
      }
    }
    
    public void onEnchantingTableUse(PlayerInteractEvent event) {
      if(!config.enableEnchanting) {
        return;
      }
      Action action = event.getAction();
      Material material = event.getClickedBlock().getType();
      boolean enchanting_table = action == Action.RIGHT_CLICK_BLOCK &&
                     material.equals(Material.ENCHANTMENT_TABLE);
      if(enchanting_table) {
        event.setCancelled(true);
      }
    }
    
    private final void handleLocations() {
        for (final Object object : new ArrayList<Object>(this.data.padlocks)) {
            final JSONObject json = (JSONObject)JSONValue.parse(object.toString());
            this.data.padlocks.add(new Location(Bukkit.getWorld(json.get((Object)"world").toString()), Double.parseDouble(json.get((Object)"x").toString()), Double.parseDouble(json.get((Object)"y").toString()), Double.parseDouble(json.get((Object)"z").toString()), Float.parseFloat(json.get((Object)"yaw").toString()), Float.parseFloat(json.get((Object)"pitch").toString())));
            this.data.padlocks.remove(object);
        }
    }
    
    public void onDisable() {
        this.getLogger().info("> SHUTTING DOWN TOTAL WAR CORE.");
        HandlerList.unregisterAll((Plugin)this);
        try {
            this.data.save();
            KeyUtils.clearFields(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
	 * A better version of dropNaturally that mimics normal drop behavior.
	 * 
	 * The built-in version of Bukkit's dropItem() method places the item at the block 
	 * vertex which can make the item jump around. 
	 * This method places the item in the middle of the block location with a slight 
	 * vertical velocity to mimic how normal broken blocks appear.
	 * @param l The location to drop the item
	 * @param is The item to drop
	 * 
	 * @author GordonFreemanQ
	 */
	public void dropItemAtLocation(final Location l, final ItemStack is) {
		
		// Schedule the item to drop 1 tick later
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				l.getWorld().dropItem(l.add(0.5, 0.5, 0.5), is).setVelocity(new Vector(0, 0.05, 0));
			}
		}, 1);
	}
    
    public void reload() {
        this.onDisable();
        this.onEnable();
    }

	public KeyAPI getAPI() {
		return this.api;
	}

	public Messages getMessages() {
		return this.messages;
	}

    public void sendMessage(final CommandSender sender, final String message) {
        sender.sendMessage(this.messages.prefix + " " + message);
		
	}
}
