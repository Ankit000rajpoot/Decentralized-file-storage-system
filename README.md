\# Decentralized File Storage System



\## Project Overview

The Decentralized File Storage System is a Java-based project that stores files by splitting them into smaller chunks and distributing them across multiple storage nodes. This approach organizes file storage and allows files to be reconstructed during download using stored metadata.



\## Features

\- File upload and download

\- Splitting files into chunks of up to 1 MB

\- Distributing chunks across multiple storage nodes

\- Metadata management for files and chunks

\- Reconstructing the original file during download



\## Technologies Used

\- Java

\- Core Java and Object-Oriented Programming (OOP)

\- Java NIO

\- File Handling

\- Collections and Exception Handling



\## Project Structure

\- `Main.java` – Entry point and user interaction

\- `FileService.java` – Handles file upload and download operations

\- `NodeManager.java` – Manages storage nodes and chunk distribution

\- `StorageNode.java` – Stores file chunks

\- `MetadataStore.java` – Manages file metadata

\- `FileMetadata.java` – Represents file information

\- `ChunkMetadata.java` – Represents chunk information



\## How It Works

1\. The user selects a file to upload.

2\. The system splits the file into chunks of up to 1 MB.

3\. The chunks are distributed across storage nodes.

4\. Metadata records the file and chunk information.

5\. During download, the system uses the metadata to retrieve and combine the chunks.



\## Current Limitations

\- The current implementation uses local storage directories.

\- Node failure recovery and chunk replication are not implemented.

\- Data availability depends on the availability of the required chunks.



\## How to Run

1\. Clone or download this repository.

2\. Import the project into Eclipse as an existing Java project.

3\. Open `src/com/ankit/dfs/Main.java`.

4\. Run the `Main` class.



\## Future Enhancements

\- Add chunk replication and fault tolerance.

\- Implement communication between nodes over a network.

\- Add file integrity verification using SHA-256.

\- Improve metadata persistence and recovery.



\## Author

Ankit



