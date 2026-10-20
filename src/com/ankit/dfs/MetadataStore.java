package com.ankit.dfs;

import java.util.*;

public class MetadataStore {

    private final Map<String, FileMetadata> files = new LinkedHashMap<>();

    public void save(FileMetadata metadata) {
        files.put(metadata.fileId(), metadata);
    }

    public FileMetadata get(String fileId) {
        return files.get(fileId);
    }

    public Collection<FileMetadata> getAll() {
        return files.values();
    }
}
