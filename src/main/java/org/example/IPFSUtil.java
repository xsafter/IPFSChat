package org.example;

import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.ipfs.api.IPFS;

public class IPFSUtil {
    private static IPFS ipfs;

    public IPFSUtil(String host, int port) {
        ipfs = new IPFS("/ip4/" + host + "/tcp/" + port);
    }

    public static String upload(String fileName, boolean pin) throws IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(fileName));
        MerkleNode addResult = ipfs.add(file).get(0);
        if (pin) {
            ipfs.pin.add(addResult.hash);
        }
        return addResult.hash.toString();
    }

    public static String upload(byte[] data, boolean pin) throws IOException {
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
        MerkleNode addResult = ipfs.add(file).get(0);
        if (pin) {
            ipfs.pin.add(addResult.hash);
        }
        return addResult.hash.toString();
    }

    public static byte[] download(String hash) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void download(String hash, String destFile) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null && data.length > 0) {
            File file = new File(destFile);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void unpin(String hash) {
        try {
            ipfs.pin.rm(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
