/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.plugins;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.progress.DefaultProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.api.utils.HttpUtils;
import com.leclercb.commons.api.utils.http.HttpResponse;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;
import com.leclercb.taskunifier.gui.components.plugins.exc.PluginException;
import com.leclercb.taskunifier.gui.components.plugins.exc.PluginException.PluginExceptionType;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginsUtils {
	
	public static void loadPlugin(File file) throws PluginException {
		if (!file.isFile()
				|| !FileUtils.getExtention(file.getAbsolutePath()).equals("jar"))
			return;
		
		try {
			File tmpFile = File.createTempFile("taskunifier_plugin_", ".jar");
			org.apache.commons.io.FileUtils.copyFile(file, tmpFile);
			
			List<SynchronizerGuiPlugin> plugins = Main.API_PLUGINS.loadJar(
					tmpFile,
					file,
					false);
			
			if (plugins.size() == 0) {
				throw new PluginException(PluginExceptionType.NO_VALID_PLUGIN);
			}
			
			if (plugins.size() > 1) {
				throw new PluginException(
						PluginExceptionType.MORE_THAN_ONE_PLUGIN);
			}
			
			try {
				if (!EqualsUtils.equals(
						Constants.PLUGIN_API_VERSION,
						plugins.get(0).getPluginApiVersion()))
					throw new PluginException(
							PluginExceptionType.OUTDATED_PLUGIN);
			} catch (Throwable t) {
				throw new PluginException(PluginExceptionType.OUTDATED_PLUGIN);
			}
			
			SynchronizerGuiPlugin plugin = plugins.get(0);
			List<SynchronizerGuiPlugin> existingPlugins = Main.API_PLUGINS.getPlugins();
			
			for (SynchronizerGuiPlugin p : existingPlugins) {
				if (EqualsUtils.equals(p.getId(), plugin.getId())
						&& EqualsUtils.equals(
								p.getVersion(),
								plugin.getVersion())) {
					throw new PluginException(PluginExceptionType.PLUGIN_FOUND);
				}
			}
			
			Main.API_PLUGINS.addPlugin(file, plugin);
			
			GuiLogger.getLogger().info(
					"Plugin loaded: "
							+ plugin.getName()
							+ " - "
							+ plugin.getVersion());
			
			return;
		} catch (PluginException e) {
			throw e;
		} catch (Throwable t) {
			throw new PluginException(PluginExceptionType.ERROR_LOADING_PLUGIN);
		}
	}
	
	public static void installPlugin(Plugin plugin, ProgressMonitor monitor)
			throws Exception {
		File file = null;
		
		try {
			if (monitor != null)
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("manage_plugins.progress.start_plugin_installation")));
			
			file = new File(Main.RESOURCES_FOLDER
					+ File.separator
					+ "plugins"
					+ File.separator
					+ UUID.randomUUID().toString()
					+ ".jar");
			
			file.createNewFile();
			
			if (monitor != null)
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("manage_plugins.progress.downloading_plugin")));
			
			org.apache.commons.io.FileUtils.copyURLToFile(
					new URL(plugin.getDownloadUrl()),
					file);
			
			if (monitor != null)
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("manage_plugins.progress.installing_plugin")));
			
			PluginsUtils.loadPlugin(file);
			
			if (monitor != null)
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("manage_plugins.progress.plugin_installed")));
			
			plugin.setStatus(PluginStatus.INSTALLED);
		} catch (PluginException e) {
			file.delete();
			throw e;
		} catch (Exception e) {
			file.delete();
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void updatePlugin(Plugin plugin, ProgressMonitor monitor)
			throws Exception {
		if (monitor != null)
			monitor.addMessage(new DefaultProgressMessage(
					Translations.getString("manage_plugins.progress.start_plugin_update")));
		
		deletePlugin(plugin, monitor);
		installPlugin(plugin, monitor);
		
		if (monitor != null)
			monitor.addMessage(new DefaultProgressMessage(
					Translations.getString("manage_plugins.progress.plugin_updated")));
	}
	
	public static void deletePlugin(Plugin plugin, ProgressMonitor monitor) {
		if (monitor != null)
			monitor.addMessage(new DefaultProgressMessage(
					Translations.getString("manage_plugins.progress.start_plugin_deletion")));
		
		List<SynchronizerGuiPlugin> existingPlugins = new ArrayList<SynchronizerGuiPlugin>(
				Main.API_PLUGINS.getPlugins());
		for (SynchronizerGuiPlugin existingPlugin : existingPlugins) {
			if (existingPlugin.getId().equals(plugin.getId())) {
				File file = Main.API_PLUGINS.getFile(existingPlugin);
				file.delete();
				Main.API_PLUGINS.removePlugin(existingPlugin);
				
				if (EqualsUtils.equals(
						Main.SETTINGS.getStringProperty("api.id"),
						existingPlugin.getId()))
					Main.SETTINGS.setStringProperty(
							"api.id",
							DummyGuiPlugin.getInstance().getId());
				
				plugin.setStatus(PluginStatus.DELETED);
			}
		}
		
		if (monitor != null)
			monitor.addMessage(new DefaultProgressMessage(
					Translations.getString("manage_plugins.progress.plugin_deleted")));
	}
	
	public static Plugin[] loadPluginsFromXML(ProgressMonitor monitor)
			throws Exception {
		try {
			if (monitor != null)
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("manage_plugins.progress.retrieve_plugin_database")));
			
			HttpResponse response = null;
			
			Boolean proxyEnabled = Main.SETTINGS.getBooleanProperty("proxy.enabled");
			if (proxyEnabled != null && proxyEnabled) {
				response = HttpUtils.getHttpGetResponse(
						new URI(Constants.PLUGINS_FILE),
						Main.SETTINGS.getStringProperty("proxy.host"),
						Main.SETTINGS.getIntegerProperty("proxy.port"),
						Main.SETTINGS.getStringProperty("proxy.login"),
						Main.SETTINGS.getStringProperty("proxy.password"));
			} else {
				response = HttpUtils.getHttpGetResponse(new URI(
						Constants.PLUGINS_FILE));
			}
			
			if (!response.isSuccessfull()) {
				throw new PluginException(
						PluginExceptionType.ERROR_LOADING_PLUGIN_DB);
			}
			
			if (monitor != null)
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("manage_plugins.progress.analysing_plugin_database")));
			
			String content = response.getContent();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			factory.setIgnoringComments(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(IOUtils.toInputStream(content));
			
			document.getDocumentElement().normalize();
			
			if (!document.getChildNodes().item(0).getNodeName().equals(
					"plugins"))
				throw new Exception("Root name must be \"plugins\"");
			
			Node root = document.getChildNodes().item(0);
			
			NodeList nPlugins = root.getChildNodes();
			
			List<Plugin> plugins = new ArrayList<Plugin>();
			
			for (int i = 0; i < nPlugins.getLength(); i++) {
				if (!nPlugins.item(i).getNodeName().equals("plugin"))
					continue;
				
				NodeList nPlugin = nPlugins.item(i).getChildNodes();
				
				String id = null;
				String name = null;
				String author = null;
				String version = null;
				String serviceProvider = null;
				String downloadUrl = null;
				
				for (int j = 0; j < nPlugin.getLength(); j++) {
					Node element = nPlugin.item(j);
					
					if (element.getNodeName().equals("id"))
						id = element.getTextContent();
					
					if (element.getNodeName().equals("name"))
						name = element.getTextContent();
					
					if (element.getNodeName().equals("author"))
						author = element.getTextContent();
					
					if (element.getNodeName().equals("version"))
						version = element.getTextContent();
					
					if (element.getNodeName().equals("serviceProvider"))
						serviceProvider = element.getTextContent();
					
					if (element.getNodeName().equals("downloadUrl"))
						downloadUrl = element.getTextContent();
				}
				
				Plugin plugin = new Plugin(
						PluginStatus.TO_INSTALL,
						id,
						name,
						author,
						version,
						serviceProvider,
						downloadUrl);
				
				plugins.add(plugin);
			}
			
			if (monitor != null)
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("manage_plugins.progress.plugin_database_retrieved")));
			
			return plugins.toArray(new Plugin[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					Translations.getString("errors.cannot_load_plugin_database"),
					e);
		}
	}
	
}
