package cn.yuuha;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Configs {
    private static final Logger logger = LogManager.getLogger(Main.class);
    static Accounts accounts = new Accounts();
    static String configPath = System.getProperty("user.home") + "\\AppData\\Roaming\\Battle.net\\";
    static Properties switcherConfigs = new Properties();
    static boolean isConfigFileExists = false;

    public static void init() {
        File configFile = new File(configPath + "Battle.net.config");
        if (configFile.exists()) {
            isConfigFileExists = true;
            long fileLength = configFile.length();
            byte[] configByte = new byte[(int) fileLength];
            try (FileInputStream fis = new FileInputStream(configFile)) {
                fis.read(configByte);
                JSONObject data = JSON.parseObject(configByte);
                String nameText = data.getJSONObject("Client").getString("SavedAccountNames");
                if (!nameText.equals("")) {
                    accounts.setAccounts(nameText);
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }
        }
        File propertiesFile = new File(configPath + "BNSwitcher.properties");
        if (propertiesFile.exists()) {
            try (FileInputStream fis = new FileInputStream(propertiesFile)) {
                switcherConfigs.load(fis);
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }
        }
    }

    public static void saveProperties() {
        File propertiesFile = new File(configPath + "BNSwitcher.properties");
        try (FileOutputStream fos = new FileOutputStream(propertiesFile)) {
            switcherConfigs.store(fos, "BattleNetAccountSwitcherProperties");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e);
            }
        }
    }

    public static void saveAccounts() {
        File configFile = new File(configPath + "Battle.net.config");
        if (configFile.exists()) {
            long fileLength = configFile.length();
            byte[] configByte = new byte[(int) fileLength];
            try (FileInputStream fis = new FileInputStream(configFile)) {
                fis.read(configByte);
                JSONObject data = JSON.parseObject(configByte);
                JSONPath.set(data, "Client.AutoLogin", "true");
                JSONPath.set(data, "Client.SavedAccountNames", accounts.toString());
                try (FileOutputStream fos = new FileOutputStream(configFile)) {
                    fos.write(JSON.toJSONBytes(data));
                } catch (Exception e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e);
                    }
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }
        }
    }
}
