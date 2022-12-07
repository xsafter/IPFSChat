package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String hash = null;
        IPFSUtil ipfsUtil = new IPFSUtil("127.0.0.1", 5001);
        try {
            hash = IPFSUtil.upload("IMG_0151.dng", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String byteData = Arrays.toString(IPFSUtil.download(hash));
        for (int i = 0; i < byteData.length(); i++) {
            System.out.print(Integer.toHexString(byteData.charAt(i)) + " ");
        }
        System.out.println();
    }
}