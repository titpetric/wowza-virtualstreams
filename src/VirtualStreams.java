package com.monotek.wms.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.File;

import java.util.List;
import java.util.Arrays;

import com.wowza.wms.server.*;
import com.wowza.wms.vhost.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.stream.publish.*;
import com.wowza.wms.logging.*;

import com.wowza.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;


public class VirtualStreams implements IServerNotify
{
	private Document ConfigData;

	public void onServerCreate(IServer server)
	{
		getLogger().info("virtualstream.start");

		String ConfigFile = "/usr/local/WowzaMediaServer/conf/virtualstreams.xml";
		File file = new File(ConfigFile);
		ConfigData = null;
		try {
			getLogger().info("virtualstream.config: Reading "+ConfigFile+".");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			ConfigData = db.parse(file);
		} catch (java.io.IOException e) {
			getLogger().warn("virtualstream.config: File "+ConfigFile+" doesn't exist.");
			ConfigData = null;
		} catch (Exception e) {
			getLogger().warn("virtualstream.config: Problem parsing "+ConfigFile+" file.");
			ConfigData = null;
		}

	}

	public void onServerInit(IServer server)
	{
		if (ConfigData == null) {
			return;
		}

		List VirtualHosts = VHostSingleton.getVHostNames();

		NodeList VirtualStreams = ConfigData.getDocumentElement().getElementsByTagName("stream");
		for (int childNum = 0; childNum < VirtualStreams.getLength(); childNum++) {
			Element child = (Element)VirtualStreams.item(childNum);
			String VHostName = child.getAttributes().getNamedItem("vhost").getNodeValue();
			String AppName = child.getAttributes().getNamedItem("app").getNodeValue();
			String FileName = child.getAttributes().getNamedItem("file").getNodeValue();
			String StreamName = child.getAttributes().getNamedItem("stream").getNodeValue();

			if (VHostName != null && AppName != null && FileName != null && StreamName != null) {
				boolean isStreamAdded = false;
				getLogger().info("virtualstreams.create vhost/appname/file/stream " + VHostName + "/" + AppName + "/" + FileName + "/" + StreamName);
				if (VirtualHosts.contains(VHostName)) {
					IVHost vhost = VHostSingleton.getInstance(VHostName);
					//List ApplicationNames = vhost.getApplicationNames();
					//if (ApplicationNames.contains(AppName)) {
						// matches vhost and application, start stream
						Stream s = Stream.createInstance(vhost, AppName, StreamName);
						s.play(FileName, 0, -1, true);
						isStreamAdded = true;
					//} else {
					//	getLogger().warn("virtualstreams.create failed, VHostName " + VHostName + ": AppName '" + AppName + "' doesn't exist.");
					//	getLogger().warn("virtualstreams.create apps: " + Arrays.toString(ApplicationNames.toArray()));
					//}
				} else {
					getLogger().warn("virtualstreams.create failed, VHostName '" + VHostName + "' doesn't exist.");
				}
			}
		}
		


		/*/ LIST vhosts = VHostSingleton.getVHostNames();
		// foreach vhosts as VHostName
		//	IVHost vhost = VHostSingleton.getInstance(VHostName);
		//	LIST apps = vhost.getApplicationNames();
		//	foreach apps as AppName
		//		foreach (virtualStreams as stream)
//					if (stream.vhost == vhost && stream.appname == AppName)
//						Stream stream1 = Stream.createInstance(vhost, AppName, stream.name);
//						stream1.play(stream.file, 0, -1, true);
//					}
//				}
	
		IVHost vhost = VHostSingleton.getInstance(VHost.VHOST_DEFAULT);

		Stream stream1 = Stream.createInstance(vhost, "live", "Stream1");
		stream1.play("mp4:Extremists.m4v", 5, 5, true);
		stream1.play("mp4:Extremists.m4v", 50, 5, false);
		stream1.play("mp4:Extremists.m4v", 150, 5, false);

		Stream stream2 = Stream.createInstance(vhost, "live", "Stream2");
		stream2.play("mp4:Extremists.m4v", 0, -1, true);
		*/
	}

	private WMSLogger getLogger()
	{
		return WMSLoggerFactory.getLogger(null);
	}

	public void onServerShutdownComplete(IServer server)
	{
	}

	public void onServerShutdownStart(IServer server)
	{
	}
}