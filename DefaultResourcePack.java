package net.minecraft.client.resources;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultResourcePack implements IResourcePack {
    private static final Set<String> RESOURCE_DOMAINS = Stream.of("minecraft").collect(Collectors.toSet());
    private static final Logger logger = LogManager.getLogger();

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) {
        try {
            return DefaultResourcePack.class.getResourceAsStream(String.format("/assets/%s/%s", resourceLocation.getResourceDomain(), resourceLocation.getResourcePath()));
        } catch (Exception e) {
            logger.error("Error loading resource: {}", resourceLocation, e);
            return null;
        }
    }

    @Override
    public boolean resourceExists(ResourceLocation resourceLocation) {
        return getInputStream(resourceLocation) != null;
    }

    @Override
    public Set<String> getResourceDomains() {
        return RESOURCE_DOMAINS;
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer metadataSerializer, String metadataSelectionName) {
        InputStream stream = getInputStream(new ResourceLocation("pack.mcmeta"));
        return AbstractResourcePack.readMetadata(metadataSerializer, stream, metadataSelectionName);
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        InputStream stream = getInputStream(new ResourceLocation("pack.png"));
        return ImageIO.read(stream);
    }

    @Override
    public String getPackName() {
        return "Default";
    }
}