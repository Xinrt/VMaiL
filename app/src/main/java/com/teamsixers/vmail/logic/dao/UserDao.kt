package com.teamsixers.vmail.logic.dao

import com.smailnet.emailkit.EmailKit
import com.smailnet.microkv.MicroKV
import java.util.*

/**
 * UserConfigDao perform storage operation for user config @see EmailKit.config information.
 *
 * @sample `val` isConfigSaved: Boolean = UserConfigDao.isUserConfigSaved()
 *
 * @author Mingyan Wang(Cyril)
 *
 * @since 1.0
 */
object UserDao {

    /**
     * Save user config into local sharedPreferences storage.
     *
     * Caller should check whether user config is saved or not.
     *
     * @param config an email account configuration
     * including email type, account, password, SSL etc @see EmailKit.Config
     *
     * @sample `if` (!isUserConfigSaved()) saveUserConfig(config)
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return save user config in local sharedPreferences storage
     */
    fun saveUserConfig(config: EmailKit.Config) : Boolean{
        MicroKV.customize("config", true)
            .setKV("account", config.account)
            .setKV("password", config.password)
            .setKV("nickname", config.nickname)
            .setKV("smtp_host", config.smtpHost)
            .setKV("imap_host", config.imapHost)
            .setKV("smtp_port", config.smtpPort)
            .setKV("imap_port", config.imapPort)
            .setKV("smtp_ssl", config.isSMTPSSL)
            .setKV("smtp_starttls", config.isSmtpSTARTTLS)
            .setKV("imap_ssl", config.isIMAPSSL)
            .save()
        return isUserConfigSaved()
    }

    /**
     * Get user config from local sharedPreferences storage.
     * Caller should check whether user config is saved or not.
     *
     * @sample `if` (isUserConfigSaved()) config = getUserConfig()
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return return a EmailKit.config that has all configuration information about an account.
     */
    fun getUserConfig(): EmailKit.Config {
        val kv = MicroKV.customize("config", true)
        return EmailKit.Config()
            .setAccount(kv.getString("account"))
            .setPassword(kv.getString("password"))
            .setNickname(kv.getString("nickname"))
            .setSMTP(
                kv.getString("smtp_host"),
                kv.getInt("smtp_port"),
                kv.getBoolean("smtp_ssl"),
                kv.getBoolean("smtp_starttls")
            )
            .setIMAP(
                kv.getString("imap_host"),
                kv.getInt("imap_port"),
                kv.getBoolean("imap_ssl")
            );
    }

    /**
     * Check user config exists in local sharedPreferences storage.
     *
     *
     * @sample `if` (isUserConfigSaved()) config = getUserConfig()
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return return true if user config exists in local storage, otherwise false.
     */
    fun isUserConfigSaved(): Boolean {
        val kv = MicroKV.customize("config", true)
        return kv.containsKV("account")
    }

    fun isFolderListSaved(): Boolean {
        val kv = MicroKV.customize("config", true)
        return kv.containsKV("folder_list")
    }

    /**
     * Caller should make sure `folder_list` exists in `config` file of local storage.
     */
    fun getFolderList(): List<String> {
        val kv = MicroKV.customize("config", true)
        return ArrayList(kv.getStringSet("folder_list"))
    }
}