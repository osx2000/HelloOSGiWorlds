package com.github.osx2000.how.app;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.osgi.framework.*;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.framework.wiring.BundleRevision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xne0133 on 22.04.2016.
 */
public class FrameworkUtil {
    private static final Logger LOG = LoggerFactory.getLogger (FrameworkUtil.class);

    private static List<Bundle> LoadBundlesFromVFSFolder(Class clazz,
                                                         String folderName,
                                                         final BundleContext bc) {
        List<Bundle> ret = new ArrayList<>();
        try {
            VirtualFile virtualFile;
            Closeable handle = null;
            URL url = clazz.getProtectionDomain().getCodeSource
                    ().getLocation();
            String protocol = url.getProtocol();
            if ("vfs".equals(protocol)) {
                URLConnection conn = url.openConnection();
                virtualFile = (VirtualFile) conn.getContent();
            } else if ("file".equals(protocol)) {
                virtualFile = VFS.getChild(url.toURI());

            } else {
                throw new UnsupportedOperationException("Protocol " + protocol + " is not supported");
            }

            List<VirtualFile> files = virtualFile
                    .getParent()
                    .getChild(folderName)
                    .getChildren();

            for (VirtualFile f : files) {
                ret.add(bc.installBundle(f.asFileURI().toString(),
                        f.openStream()));
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (BundleException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /*
    https://issues.apache.org/jira/browse/FELIX-3154
    1. Create a framework instance with the supplied config map.
    2. Call Framework.init().
    3. Register synchronous bundle listener to listen for system bundle STOPPING.
    4. Call start() on each activator in the passed in list with Framework.getBundleContext().
    5. Call Framework.start().
    Then, when a system bundle STOPPING event is received by the above synchronous listener, simply call stop() on each listener with the system bundle context.
     */

    public static Framework CreateFramework(Class anchorClass, final List<?
            extends BundleActivator> activators,
                                            Map<String, String> configuration,
                                            FrameworkFactory factory) {

        LOG.info("Initializing OSGi Config");

        Properties props = System.getProperties();
        props.setProperty("gosh.args", "--noi");
        Framework fwk = factory.newFramework(configuration);
        try {
            fwk.init();
            List<Bundle> lb = new ArrayList<>();
            final BundleContext bc = fwk.getBundleContext();
            for (Bundle b : bc.getBundles()) {
                if (!b.getLocation().startsWith("System Bundle")) {
                    b.uninstall();
                }
            }

            bc.addBundleListener(new SynchronousBundleListener() {
                @Override
                public void bundleChanged(BundleEvent event) {
                    if (event.getBundle().getBundleId() == 0) {
                        switch (event.getType()) {
                            case BundleEvent.STOPPING:
                                for(BundleActivator a : activators) {
                                    try {
                                        a.stop(event.getBundle ().getBundleContext());
                                    } catch (Exception e) {
                                        throw new RuntimeException();
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            });

            if (activators != null) {
                for (BundleActivator a : activators) {
                    a.start(bc);
                }
            }

            lb.addAll(LoadBundlesFromVFSFolder(anchorClass, "bundles", bc));

            for (Bundle b : lb) {
                if ((b.adapt(BundleRevision.class).getTypes() &
                        BundleRevision.TYPE_FRAGMENT) == 0)
                    b.start();
            }
            fwk.start();
            LOG.info("OSGi Config Initialized");

            return fwk;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }


}
