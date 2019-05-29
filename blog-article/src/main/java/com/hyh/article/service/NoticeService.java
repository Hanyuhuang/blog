package com.hyh.article.service;

import com.hyh.pojo.Notice;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.NoticeVo;
import com.hyh.pojo.Vo.PageResult;

public interface NoticeService {

    PageResult<NoticeVo> listMyNotices(Integer pageCur, Integer pageSize,Integer status, User user);

    int updateNotices(Long[] id,Integer status);

    Notice getNoticeById(Long id);

    int deleteNotices(Long[] ids);

    int countNotice(User user);
}
