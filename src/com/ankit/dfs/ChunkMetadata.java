package com.ankit.dfs;

public record ChunkMetadata(
        int sequence,
        String nodeId,
        String fileName,
        long size
) {
}
