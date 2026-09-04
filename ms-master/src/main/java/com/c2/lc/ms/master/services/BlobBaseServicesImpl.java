package com.c2.lc.ms.master.services;

import com.c2.lc.lib.base.BaseSuper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;

@Slf4j
public abstract class BlobBaseServicesImpl extends BaseSuper {


    protected static void showServerReply(FTPClient ftpClient, String method) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
            }
        }
    }

    protected String checkFolderNameIfNotExist(String name) {
        File file = new File(name);
        if (!file.exists()) {
            return name;
        }
        return String.valueOf(file);
    }

    protected String createFolderNameIfNotExist(String name) {
        File file = new File(name);
        if (!file.exists()) {
            return String.valueOf(file.mkdirs());
        }
        return String.valueOf(file);
    }
}
