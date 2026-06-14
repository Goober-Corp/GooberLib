package com.goobercorp.gooberlib.test;

import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.api.UpdateChecker;
import com.terraformersmc.modmenu.api.UpdateInfo;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ContactInformation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GooberLibTest implements ModInitializer {

	@Override
	public void onInitialize() {
		registerTestMod("yacl-test", "Yacl Parity Test Mod");
		registerTestMod("mapi-test", "MaLiLib API Parity Test Mod");
	}

	private void registerTestMod(String id, String name) {
		ModMenu.MODS.put("yacl-test", new Mod() {
			@Override
			public @NotNull String getId() {
				return id;
			}

			@Override
			public @NotNull String getName() {
				return name;
			}

			@Override
			public @NotNull DynamicTexture getIcon(FabricIconHandler iconHandler, int i) {
				return Objects.requireNonNull(iconHandler.createIcon(
						FabricLoader.getInstance()
								.getModContainer(ModMenu.MOD_ID)
								.orElseThrow(() -> new RuntimeException("Cannot get ModContainer for Fabric mod with id " + ModMenu.MOD_ID)),
						"assets/" + ModMenu.MOD_ID + "/unknown_icon.png"
				));
			}

			@Override
			public @NotNull String getDescription() {
				return name;
			}

			@Override
			public @NotNull String getVersion() {
				return "0.0.0";
			}

			@Override
			public @NotNull String getPrefixedVersion() {
				return "0.0.0";
			}

			@Override
			public @NotNull List<String> getAuthors() {
				return List.of();
			}

			@Override
			public ContactInformation getContact(String author) {
				return ContactInformation.EMPTY;
			}

			@Override
			public Map<String, Collection<String>> getContributors() {
				return Map.of();
			}

			@Override
			public SortedMap<String, Set<String>> getCredits() {
				return new TreeMap<>();
			}

			@Override
			public Set<Badge> getBadges() {
				return Set.of();
			}

			@Override
			public String getWebsite() {
				return "";
			}

			@Override
			public String getIssueTracker() {
				return "";
			}

			@Override
			public String getSource() {
				return "";
			}

			@Override
			public String getParent() {
				return "";
			}

			@Override
			public @NotNull Set<String> getLicense() {
				return Set.of();
			}

			@Override
			public @NotNull Map<String, String> getLinks() {
				return Map.of();
			}

			@Override
			public boolean isReal() {
				return false;
			}

			@Override
			public boolean allowsUpdateChecks() {
				return false;
			}

			@Override
			public @Nullable UpdateChecker getUpdateChecker() {
				return null;
			}

			@Override
			public void setUpdateChecker(@Nullable UpdateChecker updateChecker) {
			}

			@Override
			public @Nullable UpdateInfo getUpdateInfo() {
				return null;
			}

			@Override
			public void setUpdateInfo(@Nullable UpdateInfo updateInfo) {
			}

			@Override
			public void setChildHasUpdate() {
			}

			@Override
			public boolean getChildHasUpdate() {
				return false;
			}

			@Override
			public boolean isHidden() {
				return false;
			}
		});
	}
}
