package org.superbiz.moviefun.movies;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.lang.StringUtils;
import org.superbiz.moviefun.Blob;
import org.superbiz.moviefun.BlobStore;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore{

    private final AmazonS3Client s3Client;

    private final String bucketName;

    public S3Store(AmazonS3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }


    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(blob.contentType);
        s3Client.putObject(bucketName, blob.name, blob.inputStream, metadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        S3Object object = s3Client.getObject(bucketName, name);
        if(object == null){
            return Optional.empty();
        }

        String contentType = object.getObjectMetadata() != null &&
                StringUtils.isNotEmpty(object.getObjectMetadata().getContentType()) ? object.getObjectMetadata().getContentType() : "";

       return Optional.of(new Blob(name, object.getObjectContent(), contentType));

    }
}
