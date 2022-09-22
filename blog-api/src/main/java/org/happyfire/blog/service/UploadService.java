package org.happyfire.blog.service;

import org.happyfire.blog.vo.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Result upload(MultipartFile file);

    Result uploadAvatar(MultipartFile file, String token);
}
