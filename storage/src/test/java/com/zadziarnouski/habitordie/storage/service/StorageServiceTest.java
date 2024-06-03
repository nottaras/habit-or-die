package com.zadziarnouski.habitordie.storage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.mock.web.MockMultipartFile;

@Slf4j
@ExtendWith({MockitoExtension.class})
class StorageServiceTest {

    private GridFSFile mockGridFSFile;
    private InputStream mockInputStream;
    private GridFsResource mockGridFsResource;
    private GridFSFindIterable mockFindIterable;

    @Mock
    private GridFSBucket gridFSBucket;

    @Mock
    private GridFsOperations gridFsOperations;

    @InjectMocks
    private StorageService storageService;

    @Captor
    private ArgumentCaptor<String> filenameCaptor;

    @BeforeEach
    public void setup() {
        mockGridFSFile = mock(GridFSFile.class);
        mockInputStream = mock(InputStream.class);
        mockGridFsResource = mock(GridFsResource.class);
        mockFindIterable = mock(GridFSFindIterable.class);
    }

    @Test
    public void givenValidFile_whenStore_thenFileStoredSuccessfully() {
        // Given
        byte[] content = "Test file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content);
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        // When
        String fileId = storageService.store(file);

        // Then
        assertNotNull(fileId);
        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenNullInsteadOfFile_whenStore_thenIllegalArgumentExceptionThrown() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> storageService.store(null));
        assertEquals("Cannot store null or empty file", exception.getMessage());
    }

    @Test
    public void givenEmptyFile_whenStore_thenIllegalArgumentExceptionThrown() {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> storageService.store(emptyFile));
        assertEquals("Cannot store null or empty file", exception.getMessage());
    }

    @Test
    public void givenValidFile_whenStore_thenFileStoredSuccessfullyAndFilenameIsCorrect() {
        // Given
        byte[] content = "Test file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content);
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        // When
        String fileId = storageService.store(file);

        // Then
        assertNotNull(fileId);

        verify(gridFSBucket).uploadFromStream(filenameCaptor.capture(), any(InputStream.class));
        assertEquals("test.txt", filenameCaptor.getValue());

        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenFileWithNoOriginalFilename_whenStore_thenGeneratedFilenameIsUsed() {
        // Given
        byte[] content = "Test file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", null, "text/plain", content); // No original filename
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        // When
        String fileId = storageService.store(file);

        // Then
        assertNotNull(fileId);

        verify(gridFSBucket).uploadFromStream(filenameCaptor.capture(), any(InputStream.class));
        String capturedFileName = filenameCaptor.getValue();

        assertEquals(24, capturedFileName.length());
        assertTrue(capturedFileName.matches("^[a-fA-F0-9]{24}$"));

        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenFileWithEmptyOriginalFilename_whenStore_thenGeneratedFilenameIsUsed() {
        // Given
        byte[] content = "Test file content".getBytes();
        MockMultipartFile file =
                new MockMultipartFile("file", "\t\n\r\f ", "text/plain", content); // No original filename
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        // When
        String fileId = storageService.store(file);

        // Then
        assertNotNull(fileId);

        verify(gridFSBucket).uploadFromStream(filenameCaptor.capture(), any(InputStream.class));
        String capturedFileName = filenameCaptor.getValue();

        assertEquals(24, capturedFileName.length());
        assertTrue(capturedFileName.matches("^[a-fA-F0-9]{24}$"));

        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenRuntimeExceptionOnStorage_whenStore_thenRuntimeExceptionThrown() {
        // Given
        MockMultipartFile file =
                new MockMultipartFile("file", "test.txt", "text/plain", "Test file content".getBytes());
        doThrow(new RuntimeException("Failed to store file")).when(gridFSBucket).uploadFromStream(any(), any());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> storageService.store(file));
        assertNotNull(exception.getMessage());
    }

    @Test
    public void givenIOExceptionOnGetInputStream_whenStore_thenRuntimeExceptionThrown() throws IOException {
        // Given
        byte[] content = "Test file content".getBytes();
        MockMultipartFile mockFile = mock(MockMultipartFile.class);
        doThrow(new IOException()).when(mockFile).getInputStream();

        // When & Then
        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> storageService.store(mockFile), "Failed to store file");
        assertEquals("Failed to store file", exception.getMessage());
    }

    @Test
    public void givenNonExistentFileId_whenLoadAsResource_thenIllegalArgumentExceptionThrown() {
        // Given
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find((Bson) any())).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storageService.loadAsResource(objectId.toString());
        });
        assertEquals("File not found", exception.getMessage());
    }

    @Test
    public void givenValidFileIdAndResourceUnavailable_whenLoadAsResource_thenIOExceptionThrownByInputStreamRetrieval()
            throws IOException {
        // Given
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find((Bson) any())).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(mockGridFSFile);
        when(gridFsOperations.getResource(mockGridFSFile)).thenReturn(mockGridFsResource);
        doThrow(IOException.class).when(mockGridFsResource).getInputStream();

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storageService.loadAsResource(objectId.toString()),
                "File retrieval failed");
        assertEquals("File retrieval failed", exception.getMessage());
    }

    @Test
    public void givenValidFileIdAndResourceUnavailable1_whenLoadAsResource_thenIOExceptionThrownByInputStreamRetrieval()
            throws IOException {
        // Given
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find((Bson) any())).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(mockGridFSFile);
        when(gridFsOperations.getResource(mockGridFSFile)).thenReturn(mockGridFsResource);
        doThrow(RuntimeException.class).when(mockGridFsResource).getInputStream();

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storageService.loadAsResource(objectId.toString()),
                "File retrieval failed");
        assertEquals("File retrieval failed", exception.getMessage());
    }

    @Test
    public void givenValidFileId_whenLoadAsResource_thenInputStreamRetrievedAndLoggedSuccessfully() throws IOException {
        // Given
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(mockGridFSFile);
        when(gridFsOperations.getResource(mockGridFSFile)).thenReturn(mockGridFsResource);
        when(mockGridFsResource.getInputStream()).thenReturn(mockInputStream);

        // When
        Resource resource = storageService.loadAsResource(objectId.toString());

        // Then
        assertInstanceOf(InputStreamResource.class, resource);
        verify(mockGridFsResource, times(1)).getInputStream();
    }
}
