package com.ankit.dfs;

import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class Main {
    private static final Path DATA_DIR = Path.of("dfs-data");
    private static final int CHUNK_SIZE = 1024 * 1024; // 1 MB

    public static void main(String[] args) throws Exception {
        Files.createDirectories(DATA_DIR);

        NodeManager nodeManager = new NodeManager();

        nodeManager.registerNode(
                new StorageNode("node-1", DATA_DIR.resolve("node-1"), 100L * 1024 * 1024));
        nodeManager.registerNode(
                new StorageNode("node-2", DATA_DIR.resolve("node-2"), 100L * 1024 * 1024));
        nodeManager.registerNode(
                new StorageNode("node-3", DATA_DIR.resolve("node-3"), 100L * 1024 * 1024));

        MetadataStore metadataStore = new MetadataStore();
        FileService service = new FileService(nodeManager, metadataStore, CHUNK_SIZE);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== Decentralized File Storage ===");
                System.out.println("1. Upload file");
                System.out.println("2. Download file");
                System.out.println("3. List files");
                System.out.println("4. Node status");
                System.out.println("5. Exit");
                System.out.print("Choose: ");

                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> {
                            System.out.print("Enter file path: ");
                            Path input = Path.of(scanner.nextLine().trim());

                            FileMetadata metadata = service.upload(input);
                            System.out.println("Upload successful.");
                            System.out.println("File ID: " + metadata.fileId());
                            System.out.println("Checksum: " + metadata.checksum());

                            metadata.chunks().forEach(chunk ->
                                    System.out.printf(
                                            "Chunk %d -> %s (%d bytes)%n",
                                            chunk.sequence(),
                                            chunk.nodeId(),
                                            chunk.size()
                                    ));
                        }

                        case "2" -> {
                            System.out.print("Enter file ID: ");
                            String fileId = scanner.nextLine().trim();

                            System.out.print("Enter output path: ");
                            Path output = Path.of(scanner.nextLine().trim());

                            service.download(fileId, output);
                            System.out.println("Download successful: " + output);
                        }

                        case "3" -> service.listFiles();

                        case "4" -> nodeManager.printStatus();

                        case "5" -> {
                            System.out.println("Goodbye.");
                            return;
                        }

                        default -> System.out.println("Invalid choice.");
                    }
                } catch (Exception e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }
            }
        }
    }
}

