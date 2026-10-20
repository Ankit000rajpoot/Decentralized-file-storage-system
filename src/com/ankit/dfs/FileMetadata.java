package com.ankit.dfs;

import java.util.List;

public record FileMetadata(
        String fileId,
        String originalFileName,
        long fileSize,
        String checksum,
        List<ChunkMetadata> chunks
) {
}