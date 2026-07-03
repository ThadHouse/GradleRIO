package org.wpilib.gradlerio.deploy.systemcore;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

import javax.inject.Inject;
import javax.jmdns.JmDNS;

import org.wpilib.deployutils.deploy.target.RemoteTarget;
import org.wpilib.deployutils.deploy.target.discovery.action.DiscoveryAction;
import org.wpilib.deployutils.deploy.target.discovery.action.SshDiscoveryAction;
import org.wpilib.deployutils.deploy.target.location.SshDeployLocation;
import org.wpilib.deployutils.log.ETLogger;
import org.wpilib.deployutils.log.ETLoggerFactory;

public class MulticastDeployLocation extends SshDeployLocation {

    @Inject
    public MulticastDeployLocation(String name, RemoteTarget target) {
        super(name, target);
    }

    private final String serviceName = "SystemCore-FIRST";
    private final String serviceType = "_SystemCore._tcp.local.";
    private Optional<String> cachedAddress = Optional.empty();
    private final ETLogger log = ETLoggerFactory.INSTANCE.create("MulticastDeployLocation");
    private int timeout = 5000;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String getAddress() {
        return cachedAddress.get();
    }

    @Override
    public DiscoveryAction createAction() {
        return new SshDiscoveryAction(this);
    }

    @Override
    public void discover() {
        cachedAddress = Optional.of(determineAddress());
    }

    private String determineAddress() {
        log.debug("mDNS Lookup: Attempting to determine robot IP address");

        try (JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost())) {
            var serviceInfo = jmdns.getServiceInfo(serviceType, serviceName, timeout);
            if (serviceInfo != null) {
                var addresses = serviceInfo.getInet4Addresses();
                if (addresses.length > 0) {
                    String ipAddress = addresses[0].getHostAddress();
                    cachedAddress = Optional.of(ipAddress);
                    log.debug("mDNS Lookup: Found robot IP address: " + ipAddress);
                    return ipAddress;
                }
            }
            log.debug("mDNS Lookup: Could not find robot IPv4 address via mDNS");
            throw new RuntimeException("Could not find robot IPv4 address via mDNS");
        } catch (IOException e) {
            log.debug("mDNS Lookup: IOException occurred while trying to determine robot IP address");
            throw new RuntimeException(e);
        }
    }
}
