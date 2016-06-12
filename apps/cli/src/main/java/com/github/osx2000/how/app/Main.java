package com.github.osx2000.how.app;

import org.apache.commons.cli.*;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "show help");
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse( options, args );

            if (cmd.hasOption("h")) {
                help(options);
                System.exit(0);
            }

            List<? extends BundleActivator> activators =
                    new ArrayList<>();

            Map<String,String> configuration = createConfig();

            FrameworkFactory factory = ServiceLoader.load(FrameworkFactory.
                    class).iterator().next();

            Framework fwk = FrameworkUtil.CreateFramework(Main.class,activators,configuration,factory);

            fwk.start();

            fwk.waitForStop(0);
            System.exit(0);

        }
        catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

    private static void help(Options options) {
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("Main", options);
        System.exit(0);
    }

    private static Map<String, String> createConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("felix.auto.deploy.action", "uninstall");
        config.put("org.osgi.framework.storage.clean", "onFirstInit");
        config.put("org.osgi.framework.bootdelegation", "sun.*");
        config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
                join(Arrays.asList(exportPackages),","));

        return config;
    }

    private static String[] exportPackages = new String[] {
            "sun.misc",
            "sun.security.util",
            "sun.security.x509",
            "com.sun.management",
            "com.sun.nio.sctp",
            "sun.reflect",
         //   "javax.transaction.xa;version=1.0",
         //   "javax.transaction.xa;version=1.1",
         //   "javax.transaction.xa;version=1.2",
         //   "javax.transaction;version=1.0",
         //   "javax.transaction;version=1.1",
         //   "javax.transaction;version=1.2",
            "javax.xml.stream;version=1.0",
            "javax.xml.stream.events;version=1.0",
            "javax.xml.stream.util;version=1.0",
    };


    private static String join(Collection<String> strings, String
            separator) {
        Iterator<String> iter = strings.iterator();
        StringBuilder sb = new StringBuilder();
        if (iter.hasNext()) {
            sb.append(iter.next());
            while (iter.hasNext()) {
                sb.append(separator).append(iter.next());
            }
        }
        String joined = sb.toString();
        return joined;
    }

}