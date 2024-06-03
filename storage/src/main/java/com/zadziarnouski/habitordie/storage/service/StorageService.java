package com.zadziarnouski.habitordie.storage.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final GridFSBucket gridFSBucket;
    private final GridFsOperations gridFsOperations;

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("Failed to store null or empty file");
            throw new IllegalArgumentException("Cannot store null or empty file");
        }

        ObjectId uploadedFileId;
        var fileName = (file.getOriginalFilename() != null
                        && !file.getOriginalFilename().isBlank())
                ? file.getOriginalFilename()
                : new ObjectId().toHexString();

        try (InputStream inputStream = file.getInputStream()) {
            uploadedFileId = gridFSBucket.uploadFromStream(fileName, inputStream);
        } catch (IOException e) {
            log.error("Failed to store file {}: ", fileName, e);
            throw new RuntimeException("Failed to store file", e);
        }

        log.info("File stored successfully with name: {}", fileName);
        return uploadedFileId.toString();
    }

    public Resource loadAsResource(String fileId) {
        try {
            var objectId = new ObjectId(fileId);
            var gridFSFile = getFileByObjectId(objectId);

            if (gridFSFile == null) {
                log.error("File not found for ID: {}", fileId);
                throw new IllegalArgumentException("File not found");
            }

            InputStream inputStream = getInputStreamFromFile(gridFSFile);
            log.info("File retrieved successfully for ID: {}", fileId);
            return new InputStreamResource(inputStream);
        } catch (IllegalArgumentException e) {
            log.error("Invalid file ID {}: ", fileId, e);
            throw e;
        } catch (IOException e) {
            log.error("Failed to retrieve file for ID {}: ", fileId, e);
            throw new RuntimeException("File retrieval failed", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving file for ID {}: ", fileId, e);
            throw new RuntimeException("File retrieval failed", e);
        }
    }

    private GridFSFile getFileByObjectId(ObjectId objectId) {
        return gridFSBucket.find(Filters.eq("_id", objectId)).first();
    }

    private InputStream getInputStreamFromFile(GridFSFile file) throws IOException {
        return gridFsOperations.getResource(file).getInputStream();
    }
}
