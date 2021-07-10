package io.chocorean.authmod.config;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.FactoryConfig;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import io.chocorean.authmod.core.i18n.ServerLanguageMap;

import java.nio.file.Path;
import java.util.*;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@net.minecraftforge.common.config.Config(modid = AuthMod.MODID)
@Mod.EventBusSubscriber(modid = AuthMod.MODID)
public class Config {

  @Comment("Email will be asked to the player for registration and authentication")
  @net.minecraftforge.common.config.Config.Name("identifierRequired")
  public static boolean identifierRequired = false;

  @Comment("If the player doesn't log in after this delay (the unit is the second), he will be kicked from the server.")
  @net.minecraftforge.common.config.Config.Name("delay")
  public static int delay = 60;

  @Comment("Enable or disable the /login command. If disabled, the server will be opened to everyone).")
  @net.minecraftforge.common.config.Config.Name("enableLogin")
  public static boolean enableLogin = false;

  @Comment("Enable or disable the /register command")
  @net.minecraftforge.common.config.Config.Name("enableRegister")
  public static boolean enableRegister = false;

  @Comment("Enable or disable the /changepassword command")
  @net.minecraftforge.common.config.Config.Name("enableChangePassword")
  public static boolean enableChangePassword = false;

  @Comment("lang file to use")
  @net.minecraftforge.common.config.Config.Name("language")
  public static ServerLanguageMap.Language language = ServerLanguageMap.Language.EN_US;

  @Comment("The way you want to store player's data.")
  @net.minecraftforge.common.config.Config.Name("dataSource")
  public static DataSourceStrategyInterface.Strategy dataSource = DataSourceStrategyInterface.Strategy.FILE;

  public static final DatabaseConfig database = new DatabaseConfig();

  public static final I18nConfig i18n = new I18nConfig();

  @Comment("allowed commands (can be used without being logged)")
  @net.minecraftforge.common.config.Config.Name("allowlist")
  public static String[] allowlist = new String[] { "register", "login", "logged", "help" };

  public static Path getConfigurationFile() {
    return Loader.instance().getConfigDir().toPath();
  }

  public static FactoryConfig getFactoryConfig() {
    Map<DatabaseStrategy.Column, String> columns = new HashMap<>();
    columns.put(DatabaseStrategy.Column.PASSWORD, Config.database.columnPassword);
    columns.put(DatabaseStrategy.Column.USERNAME, Config.database.columnUsername);
    columns.put(DatabaseStrategy.Column.BANNED, Config.database.columnBanned);
    columns.put(DatabaseStrategy.Column.UUID, Config.database.columnUuid);
    return new FactoryConfig()
      .setConfigDirectory(getConfigurationFile().resolve("..").normalize())
      .setStrategy(Config.dataSource)
      .setIdentifierRequired(Config.identifierRequired)
      .setDialect(database.dialect)
      .setDatabase(database.database)
      .setTable(database.table)
      .setHost(database.host)
      .setPort(database.port)
      .setUser(database.user)
      .setDriver(database.driver)
      .setPassword(database.password)
      .setColumns(columns);
  }

  public static boolean authmodEnabled() {
    return enableLogin || enableRegister;
  }

  @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
  public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(AuthMod.MODID)) {
      ConfigManager.sync(AuthMod.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
      setup();
    }
  }

  private static void setup() {
    ServerLanguageMap.loadTranslations(language.name());
    ServerLanguageMap.replaceTranslations(i18n.getTranslations());
  }

  public static void load(FMLPreInitializationEvent event) {
    // File configFile = event.getSuggestedConfigurationFile();
    setup();
  }
}