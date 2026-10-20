package com.ankit.dfs;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

public class FileService {

    private final NodeManager nodeManager;
    private final MetadataStore metadataStore;
    private final int chunkSize;

    public FileService(
            NodeManager nodeManager,
            MetadataStore metadataStore,
            int chunkSize) {

        this.nodeManager = nodeManager;
        this.metadataStore = metadataStore;
        this.chunkSize = chunkSize;
    }

    public FileMetadata upload(Path input)
            throws Exception {

        if (!Files.exists(input)) {
            throw new FileNotFoundException(
                    "File not found: " + input
            );
        }

        if (!Files.isRegularFile(input)) {
            throw new IOException(
                    "Path is not a regular file."
            );
        }

        String fileId = UUID.randomUUID().toString();

        List<StorageNode> nodes = nodeManager.getNodes();

        if (nodes.isEmpty()) {
            throw new IOException("No storage nodes available.");
        }

        List<ChunkMetadata> chunks = new ArrayList<>();

        long fileSize = Files.size(input);

        byte[] allData = Files.readAllBytes(input);

        String checksum = calculateSHA256(allData);

        int sequence = 0;
        int nodeIndex = 0;

        for (int start = 0; start < allData.length; start += chunkSize) {

            int end = Math.min(
                    start + chunkSize,
                    allData.length
            );

            byte[] chunkData = Arrays.copyOfRange(
                    allData,
                    start,
                    end
            );

            StorageNode node = nodes.get(
                    nodeIndex % nodes.size()
            );

            String chunkFileName =
                    fileId + "_chunk_" + sequence;

            node.storeChunk(chunkFileName, chunkData);

            chunks.add(
                    new ChunkMetadata(
                            sequence,
                            node.getNodeId(),
                            chunkFileName,
                            chunkData.length
                    )
            );

            sequence++;
            nodeIndex++;
        }

        FileMetadata metadata = new FileMetadata(
                fileId,
                input.getFileName().toString(),
                fileSize,
                checksum,
                chunks
        );

        metadataStore.save(metadata);

        return metadata;
    }

    public void download(
            String fileId,
            Path output)
            throws Exception {

        FileMetadata metadata =
                metadataStore.get(fileId);

        if (metadata == null) {
            throw new IOException(
                    "File ID not found: " + fileId
            );
        }

        try (OutputStream out =
                     Files.newOutputStream(
                             output,
                             StandardOpenOption.CREATE,
                             StandardOpenOption.TRUNCATE_EXISTING)) {

            for (ChunkMetadata chunk : metadata.chunks()) {

                StorageNode node =
                        nodeManager.getNode(chunk.nodeId());

                if (node == null) {
                    throw new IOException(
                            "Storage node not found: "
                                    + chunk.nodeId()
                    );
                }

                byte[] data =
                        node.readChunk(chunk.fileName());

                out.write(data);
            }
        }

        byte[] downloadedData =
                Files.readAllBytes(output);

        String checksum =
                calculateSHA256(downloadedData);

        if (!checksum.equals(metadata.checksum())) {

            throw new IOException(
                    "Checksum verification failed."
            );
        }
    }

    public void listFiles() {

        System.out.println("\n=== Stored Files ===");

        Collection<FileMetadata> files =
                metadataStore.getAll();

        if (files.isEmpty()) {
            System.out.println("No files uploaded.");
            return;
        }

        for (FileMetadata file : files) {

            System.out.println(
                    "File ID: " + file.fileId()
            );

            System.out.println(
                    "Name: " + file.originalFileName()
            );

            System.out.println(
                    "Size: " + file.fileSize() + " bytes"
            );

            System.out.println(
                    "Chunks: " + file.chunks().size()
            );

            System.out.println(
                    "Checksum: " + file.checksum()
            );

            System.out.println();
        }
    }

    private String calculateSHA256(byte[] data)
            throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

        byte[] hash =
                digest.digest(data);

        StringBuilder hex =
                new StringBuilder();

        for (byte b : hash) {
            hex.append(
                    String.format("%02x", b)
            );
        }

        return hex.toString();
    }
}