package com.programmerdan.minecraft.wordbank;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.InvalidPluginException;

import com.programmerdan.minecraft.devotion.Devotion;

public class WordBankConfig {
	private static int expected_config_level = 1;
	
	private WordList words;
	private int activation_length;
	private ItemStack cost;
	private CharConfig color;
	private int word_max;
	private CharConfig word_count;
	private CharConfig[] word_config;
	
	public WordBankConfig(ConfigurationSection config) throws InvalidPluginException {
		
		int actual_config_level = config.getInt("configuration_file_version", -1);
		if (actual_config_level < 0 || actual_config_level > WordBankConfig.expected_config_level) {
			throw new org.bukkit.plugin.InvalidPluginException("Invalid configuration file");
		}
		
		this.cost = config.getItemStack("cost", new ItemStack(Material.EXP_BOTTLE, 10));
		
		try (InputStream words = WordBank.instance().getResource(config.getString("wordlist_file"))){
			this.words = new WordList(words);
		} catch (IOException e) {
			WordBank.log().log(Level.SEVERE, "Failed to load word list.", e);
		}
		
		this.activation_length = config.getInt("activation_length", 10);
		
		this.word_max = config.getInt("word.max", 3);
		this.color = new CharConfig(config.getConfigurationSection("color"), activation_length);
		this.word_count = new CharConfig(config.getConfigurationSection("word.count"), activation_length);
		this.word_config = new CharConfig[this.word_max];
		for (int a = 0; a < this.word_max; a++) {
			this.word_config[a] = new CharConfig(config.getConfigurationSection("word." + (a+1)), activation_length);
		}
	}

}