package org.happyfire.blog.service;

import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.ChangePasswordParam;

public interface ChangeService {
    Result changePassword(ChangePasswordParam changePasswordParam);
}
