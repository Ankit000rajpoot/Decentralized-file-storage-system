package com.ankit.dfs;

import java.io.IOException;
import java.nio.file.*;

public class StorageNode {

    private final String nodeId;
    private final Path storagePath;
    private final long capacity;
    private long usedSpace;

    public StorageNode(String nodeId, Path storagePath, long capacity)
            throws IOException {

        this.nodeId = nodeId;
        this.storagePath = storagePath;
        this.capacity = capacity;

        Files.createDirectories(storagePath);

        calculateUsedSpace();
    }

    private void calculateUsedSpace() throws IOException {
        usedSpace = 0;

        try (var stream = Files.walk(storagePath)) {
            for (Path path : (Iterable<Path>) stream::iterator) {
                if (Files.isRegularFile(path)) {
                    usedSpace += Files.size(path);
                }
            }
        }
    }

    public String getNodeId() {
        return nodeId;
    }

    public Path getStoragePath() {
        return storagePath;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    public boolean hasSpace(long size) {
        return usedSpace + size <= capacity;
    }

    public Path storeChunk(String fileName, byte[] data)
            throws IOException {

        if (!hasSpace(data.length)) {
            throw new IOException("Not enough storage on node: " + nodeId);
        }

        Path chunkPath = storagePath.resolve(fileName);

        Files.write(
                chunkPath,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        usedSpace += data.length;

        return chunkPath;
    }

    public byte[] readChunk(String fileName)
            throws IOException {

        Path chunkPath = storagePath.resolve(fileName);

        if (!Files.exists(chunkPath)) {
            throw new IOException("Chunk not found: " + fileName);
        }

        return Files.readAllBytes(chunkPath);
    }
}
