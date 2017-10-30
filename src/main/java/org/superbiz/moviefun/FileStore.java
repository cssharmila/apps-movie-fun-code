package org.superbiz.moviefun;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.*;
import java.util.Optional;

public class FileStore implements BlobStore {

    private final Tika tika = new Tika();

    @Override
    public void put(Blob blob) throws IOException{
        File targetFile = new File(blob.name);
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(blob.inputStream, outputStream);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException{
        File coverFile = new File(name);
        if(coverFile.exists()){
            return Optional.of(new Blob(name, new FileInputStream(coverFile), tika.detect(coverFile)));
        }else{
            return Optional.empty();
        }
    }

}
